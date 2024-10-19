package com.example.tictactoe.network

import com.example.tictactoe.models.ActionRequest
import com.example.tictactoe.models.AppState
import com.example.tictactoe.models.CellState
import com.example.tictactoe.models.Opponent
import com.example.tictactoe.models.ServiceResponse
import com.example.tictactoe.network.actions.GetAvailableGamesPayload
import com.example.tictactoe.network.actions.JoinGamePayload
import com.example.tictactoe.network.actions.QuitGamePayload
import com.example.tictactoe.network.actions.UpdateGamePayload
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class TicTacToeService(
    private val client: HttpClient,
) {
    private var webSocketSession: DefaultClientWebSocketSession? = null
    private val _appState = MutableStateFlow(AppState())
    val appState = _appState.asStateFlow()
    private val job = Job()
    private val checkConnectionScope = CoroutineScope(job)

    suspend fun connect() {
        updateConnectionState()
        try {
            client.webSocket("wss://roasted-nani-mohammad-al-refaai-de14128b.koyeb.app/ws") {
                webSocketSession = this
                println("---Connected: ${webSocketSession?.isActive}")
                checkConnectionScope.launch {
                    checkConnection()
                }
                webSocketSession!!
                    .incoming
                    .consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull { Json.decodeFromString<ServiceResponse>(it.readText()) }
                    .collect { response ->
                        handleResponse(response, _appState)
                    }
            }
        } catch (e: Exception) {
            handleDisconnection(e)
        }
    }

    private suspend fun checkConnection() {
        while (true) {
            if (webSocketSession != null) {
                if (!webSocketSession!!.isActive) {
                    handleDisconnection(Throwable("Websocket Session is not Active"))
                    break
                }
            }
            delay(1000L)
        }
    }

    private fun updateConnectionState()  {
        _appState.update {
            it.copy(
                isConnected = false,
                isConnectionError = false,
                isLoading = true,
                isGetAvailableGamesLoading = false,
            )
        }
    }

    suspend fun getAvailableGames() {
        _appState.update {
            it.copy(isGetAvailableGamesLoading = true)
        }
        try {
            val message =
                Json.encodeToString(
                    GetAvailableGamesPayload.serializer(),
                    GetAvailableGamesPayload(
                        action = ActionRequest.GET_AVAILABLE_GAMES,
                        clientId = appState.value.clientId!!,
                    ),
                )
            println("Call getAvailableGames")
            sendMessage(message)
        } catch (e: Exception) {
            handleDisconnection(e)
        }
    }

    suspend fun joinGame(gameId: String) {
        _appState.update {
            it.copy(isJoiningGame = true)
        }
        try {
            val message =
                Json.encodeToString(
                    JoinGamePayload.serializer(),
                    JoinGamePayload(
                        action = ActionRequest.JOIN_GAME,
                        clientId = appState.value.clientId!!,
                        gameId = gameId,
                    ),
                )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            handleDisconnection(e)
        }
    }

    suspend fun updateGame(
        row: Int,
        col: Int,
    ) {
        try {
            val message =
                Json.encodeToString(
                    UpdateGamePayload.serializer(),
                    UpdateGamePayload(
                        action = ActionRequest.UPDATE_GAME,
                        clientId = appState.value.clientId!!,
                        gameId = appState.value.gameId!!,
                        row = row,
                        column = col,
                    ),
                )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            handleDisconnection(e)
        }
    }

    suspend fun quitGame() {
        try {
            val message =
                Json.encodeToString(
                    QuitGamePayload.serializer(),
                    QuitGamePayload(
                        action = ActionRequest.QUIT_GAME,
                        clientId = appState.value.clientId!!,
                        gameId = appState.value.gameId!!,
                    ),
                )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            handleDisconnection(e)
        }
    }

    // Send message to WebSocket
    private suspend fun sendMessage(message: String) {
        webSocketSession?.send(Frame.Text(message))
    }

    // Close WebSocket connection
    suspend fun close() {
        webSocketSession?.close()
        webSocketSession = null
        println("WebSocket disconnected!")
    }

    private suspend fun handleDisconnection(e: Throwable) {
        println("DISCONNECTED: cause: $e")
        _appState.emit(AppState(isConnectionError = true, isConnected = false))
    }

    fun resetError() {
        _appState.update {
            it.copy(error = null)
        }
    }

    fun resetAvailableGames() {
        _appState.update {
            it.copy(availableGames = emptyList())
        }
    }

    fun resetGameState() {
        _appState.update {
            it.copy(
                isLoseCurrentGame = false,
                isOpponentQuitGame = false,
                isGameFinished = false,
                isJoinedGame = false,
                isGameStarted = false,
                isWinCurrentGame = false,
                opponent = Opponent("", ""),
                myCellState = CellState.NONE,
                error = null,
                gameId = null,
                playIdTurn = null,
                turn = null,
                board = emptyArray(),
            )
        }
    }
}
