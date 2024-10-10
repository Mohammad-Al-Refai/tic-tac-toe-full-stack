package com.example.tictactoe.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.models.ActionResponse
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.Actions.GetAvailableGamesPayload
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json


class TicTacToeService(private val client: HttpClient) : ViewModel() {
    var webSocketSession: DefaultClientWebSocketSession? = null
    private val _gameStateFlow =
        MutableStateFlow(GameState())  // Initialize with default state
    val gameStateFlow: StateFlow<GameState> get() = _gameStateFlow
    private val job = Job()
    private val handleConnectionError = CoroutineExceptionHandler { _, exception ->
        println("Connection error: ${exception.message}")
    }

    fun connect() {
        CoroutineScope(Dispatchers.IO + job).launch(handleConnectionError) {
            supervisorScope {
                _gameStateFlow.update {
                    it.copy(
                        isConnected = false,
                        isConnectionError = false,
                        isLoading = true,
                        availableGames = emptyList(),
                        clientId = null
                    )
                }
                client.webSocket("wss://roasted-nani-mohammad-al-refaai-de14128b.koyeb.app/ws") {
                    webSocketSession = this
                    println("---Connected: ${webSocketSession?.isActive}")
                    observeSocketMessages()
                }
            }
        }
    }


    fun getAvailableGames() {
        viewModelScope.launch {
            if (_gameStateFlow.value.clientId == null) {
                return@launch
            }
            println("---Getting available games: clientId: ${_gameStateFlow.value.clientId}")
            try {
                sendMessage(
                    Json.encodeToString(
                        GetAvailableGamesPayload.serializer(),
                        GetAvailableGamesPayload(clientId = _gameStateFlow.value.clientId!!)
                    )
                )
            } catch (e: Exception) {
                println("Error while send GetAvailableGamesPayload : ${e.message}")
                throw Error(e)
            }
        }
    }

    private fun observeSocketMessages() {
        viewModelScope.launch {
            webSocketSession!!.incoming.consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<GameResponse>(it.readText()) }
                .collect { gameState ->
                    println("---Response: $gameState")
                    when (gameState.action) {
                        ActionResponse.AVAILABLE_GAMES -> {
                            println("---Available games: ${gameState.games}")
                            _gameStateFlow.update {
                                it.copy(availableGames = gameState.games!!)
                            }
                        }

                        ActionResponse.ERROR -> {
                            println("---Response: $gameState.errorMessage")
                            _gameStateFlow.update {
                                it.copy(error = gameState.errorMessage!!)
                            }
                        }

                        ActionResponse.CONNECTED -> {
                            println("---CONNECTED: ${gameState.clientId}")
                            _gameStateFlow.update {
                                it.copy(
                                    clientId = gameState.clientId,
                                    isConnected = true,
                                    isLoading = false,
                                    isConnectionError = false
                                )
                            }
                        }

                        ActionResponse.GAME_CREATED -> TODO()
                        ActionResponse.JOINED_GAME -> TODO()
                        ActionResponse.NEW_PLAYER_JOINED -> TODO()
                        ActionResponse.UPDATE_GAME -> TODO()
                        ActionResponse.PLAYER_QUIT -> TODO()
                        ActionResponse.WIN -> TODO()
                        ActionResponse.DRAW -> TODO()
                        ActionResponse.NONE -> TODO()
                    }
                }
        }
    }

    // Send message to WebSocket
    private  fun sendMessage(message: String) {
        viewModelScope.launch {
            webSocketSession?.send(Frame.Text(message))
        }
    }


    // Close WebSocket connection
    suspend fun close() {
        webSocketSession?.close()
        webSocketSession = null
        println("WebSocket disconnected!")
    }
}
