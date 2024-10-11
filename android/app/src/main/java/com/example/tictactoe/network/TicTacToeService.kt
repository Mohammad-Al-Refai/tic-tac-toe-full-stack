package com.example.tictactoe.network

import com.example.tictactoe.models.ActionRequest
import com.example.tictactoe.models.ActionResponse
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.Actions.GetAvailableGamesPayload
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json


class TicTacToeService(private val client: HttpClient) {
    private var webSocketSession: DefaultClientWebSocketSession? = null
    private val _gameState = MutableStateFlow(GameState())
    val gameState = _gameState.asStateFlow()
    suspend fun connect() {
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
                                        isLoading = false
                                    )
                                }

                            }

                            ActionResponse.AVAILABLE_GAMES -> {
                                _gameState.update {
                                    it.copy(availableGames = response.games.orEmpty())
                                }
                            }

                            ActionResponse.NONE -> TODO()
                            ActionResponse.ERROR -> TODO()
                            ActionResponse.GAME_CREATED -> TODO()
                            ActionResponse.JOINED_GAME -> TODO()
                            ActionResponse.NEW_PLAYER_JOINED -> TODO()
                            ActionResponse.UPDATE_GAME -> TODO()
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
        println("isWebSocketActive?: ${webSocketSession!!.isActive}")
        println("---Getting available games: clientId: ${gameState.value.clientId}")
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
}
