package com.example.tictactoe.network

import com.example.tictactoe.models.ActionRequest
import com.example.tictactoe.models.ActionResponse
import com.example.tictactoe.models.CellState
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.models.Opponent
import com.example.tictactoe.network.Actions.GetAvailableGamesPayload
import com.example.tictactoe.network.Actions.JoinGamePayload
import com.example.tictactoe.network.Actions.UpdateGamePayload
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json


class TicTacToeService(private val client: HttpClient) {
    private var webSocketSession: DefaultClientWebSocketSession? = null
    private val _gameState = MutableStateFlow(GameState())
    val gameState = _gameState.asStateFlow()
    suspend fun connect() {
        _gameState.update {
            it.copy(
                isConnected = false,
                isConnectionError = false,
                isLoading = true,
                isGetAvailableGamesLoading = false
            )
        }
        try {
            client.webSocket("wss://roasted-nani-mohammad-al-refaai-de14128b.koyeb.app/ws") {
                webSocketSession = this
                println("---Connected: ${webSocketSession?.isActive}")
                webSocketSession!!.incoming.consumeAsFlow()
                    .filterIsInstance<Frame.Text>()
                    .mapNotNull { Json.decodeFromString<GameResponse>(it.readText()) }
                    .collect { response ->
                        println("---Response: $response")
                        when (response.action) {
                            ActionResponse.CONNECTED -> {
                                _gameState.update {
                                    it.copy(
                                        clientId = response.clientId,
                                        isConnected = true,
                                        isLoading = false,
                                        clientName = response.name!!
                                    )
                                }

                            }

                            ActionResponse.AVAILABLE_GAMES -> {
                                _gameState.update {
                                    it.copy(
                                        availableGames = response.games.orEmpty(),
                                        isGetAvailableGamesLoading = false
                                    )
                                }
                            }

                            ActionResponse.NONE -> TODO()
                            ActionResponse.ERROR -> TODO()
                            ActionResponse.GAME_CREATED -> TODO()
                            ActionResponse.JOINED_GAME -> {
                                _gameState.update {
                                    it.copy(
                                        isGameReady = true,
                                        gameId = response.game!!.id,
                                        board = response.game!!.board,
                                        opponent = getOpponent(response.game!!),
                                        myCellState = getMyCellState(response.game!!)
                                    )
                                }
                            }

                            ActionResponse.NEW_PLAYER_JOINED -> TODO()
                            ActionResponse.UPDATE_GAME -> {
                                _gameState.update {
                                    it.copy(
                                        playIdTurn = response.playerIdTurn,
                                        turn = response.turn,
                                        board = response.board
                                    )
                                }
                            }

                            ActionResponse.PLAYER_QUIT -> TODO()
                            ActionResponse.WIN -> TODO()
                            ActionResponse.DRAW -> TODO()
                        }
                    }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }


    suspend fun getAvailableGames() {
        _gameState.update {
            it.copy(isGetAvailableGamesLoading = true)
        }
        try {
            val message = Json.encodeToString(
                GetAvailableGamesPayload.serializer(),
                GetAvailableGamesPayload(
                    action = ActionRequest.GET_AVAILABLE_GAMES,
                    clientId = gameState.value.clientId!!
                )
            )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            throw Error(e)
        }
    }

    suspend fun joinGame(gameId: String) {
        _gameState.update {
            it.copy(isGetAvailableGamesLoading = true)
        }
        try {
            val message = Json.encodeToString(
                JoinGamePayload.serializer(),
                JoinGamePayload(
                    action = ActionRequest.JOIN_GAME,
                    clientId = gameState.value.clientId!!,
                    gameId = gameId
                )
            )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            throw Error(e)
        }
    }

    suspend fun updateGame(row: Int, col: Int) {
        try {
            val message = Json.encodeToString(
                UpdateGamePayload.serializer(),
                UpdateGamePayload(
                    action = ActionRequest.UPDATE_GAME,
                    clientId = gameState.value.clientId!!,
                    gameId = gameState.value.gameId!!,
                    row = row,
                    column = col
                )
            )
            println(message)
            sendMessage(message)
        } catch (e: Exception) {
            throw Error(e)
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

    private fun getOpponent(game: Game): Opponent {
        var name = ""
        var id = ""
        if (game.playerId1 != gameState.value.clientId) {
            name = game.player1Name!!
            id = game.playerId1!!
        }
        if (game.playerId2 != gameState.value.clientId) {
            name = game.player2Name!!
            id = game.playerId2!!
        }
        return Opponent(name, id)
    }

    private fun getMyCellState(game: Game): CellState {
        if (game.playerId1 == gameState.value.clientId) {
            return CellState.X
        }
        if (game.playerId2 == gameState.value.clientId) {
            return CellState.O
        }
        return CellState.NONE
    }
}
