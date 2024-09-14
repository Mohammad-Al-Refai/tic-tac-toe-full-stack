package com.mohammad.tic_tac_toe

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tic_tac_toe.models.ActionType
import com.mohammad.tic_tac_toe.services.GameService
import com.mohammad.tic_tac_toe.services.PlayerService
import com.mohammad.tic_tac_toe.services.SessionService
import com.mohammad.tic_tac_toe.utils.messageToWsCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class WebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val scope: CoroutineScope,
    private val playerService: PlayerService,
    private val gameService: GameService,
    private val sessionService: SessionService,
) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        scope.launch {
            playerService.createPlayer(session)
            println("PLAYER CONNECTED")
        }
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage,
    ) {
        val messageText = message.payload
        val command = messageToWsCommand(messageText, objectMapper)
        if (command == null) {
            sessionService.sendError(session, "Invalid payload", requestId = "")
            return
        }

        scope.launch {
            when (command.action) {
                ActionType.CREATE_GAME -> gameService.createGame(session, command.requestId)
                ActionType.JOIN_GAME -> playerService.joinGame(session, command.gameId, command.requestId)
                ActionType.QUIT_GAME -> gameService.quitGame(session)
                ActionType.GET_AVAILABLE_GAMES -> gameService.getAvailableGames(session, command.requestId)
                ActionType.UPDATE_GAME ->
                    gameService.updateGame(
                        session,
                        command.requestId,
                        command.gameId,
                        command.row,
                        command.column,
                    )
            }
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus,
    ) {
        scope.launch { playerService.deactivate(session) }
    }
}
