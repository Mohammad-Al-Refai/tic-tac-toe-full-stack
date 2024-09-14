package com.mohammad.tic_tac_toe.services

import com.mohammad.tec.tac_toe.models.Player
import com.mohammad.tec.tac_toe.repo.PlayerRepo
import com.mohammad.tec.tac_toe.responses.PlayerConnected
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.*

@Service
class PlayerService(
    private val playerRepo: PlayerRepo,
    private var sessionService: SessionService,
    private var gameService: GameService
) {
    suspend fun createPlayer(session: WebSocketSession) {
        sessionService.setPlayerName(session, generatePlayerName(session.id))
        sessionService.addSession(session)
        val player = Player(
            name = sessionService.getPlayerName(session),
            isActive = true,
            sessionId = sessionService.getSessionId(session)
        )
        val savedPlayer = playerRepo.save(player)
        sessionService.setPlayerId(session, savedPlayer.id!!)
        sessionService.sendMessage(session, PlayerConnected(clientId = savedPlayer.id, name = savedPlayer.name))
    }

    suspend fun deactivate(session: WebSocketSession) {
        gameService.playerQuitNotification(session)
        gameService.deleteGame(session)
        playerRepo.deleteBySessionId(sessionService.getSessionId(session))
        sessionService.removeSession(session)
    }

    suspend fun joinGame(session: WebSocketSession, gameId: UUID?, requestId: String) =
        gameService.joinGame(session, gameId, requestId)

    private fun generatePlayerName(id: String): String {
        return "guest_${id.substring(0, 5)}"
    }
}