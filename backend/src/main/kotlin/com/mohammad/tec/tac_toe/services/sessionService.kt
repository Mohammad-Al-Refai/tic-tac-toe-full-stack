package com.mohammad.tec.tac_toe.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tec.tac_toe.models.ActionResponse
import com.mohammad.tec.tac_toe.models.GameError
import com.mohammad.tec.tac_toe.models.IGameResponse
import com.mohammad.tec.tac_toe.utils.dtoToByteArray
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.*


@Service
class SessionService(
    private val objectMapper: ObjectMapper,
) {
    private val activePlayers = mutableMapOf<UUID, WebSocketSession>()
    private val activeSessionsIds = mutableSetOf<String>()
    private val lock = Any()
    fun addSession(session: WebSocketSession) {
        activeSessionsIds.add(session.id)
    }

    fun removeSession(session: WebSocketSession) {
        activeSessionsIds.remove(session.id)
        activePlayers.remove(getPlayerId(session))
    }


    fun setPlayerName(session: WebSocketSession, playerName: String) {
        session.attributes["name"] = playerName
    }


    fun setPlayerId(session: WebSocketSession, playerId: UUID) {
        session.attributes["id"] = playerId
        activePlayers[playerId] = session
    }


    fun setGameId(session: WebSocketSession, gameId: UUID) {
        session.attributes["gameId"] = gameId
    }

    fun sendError(session: WebSocketSession, message: String) {
        if (!session.isOpen) {
            return
        }
        val response = GameError(action = ActionResponse.ERROR, errorMessage = message)
        session.sendMessage(
            TextMessage(
                dtoToByteArray(
                    response,
                    objectMapper
                )
            )
        )
    }

    fun sendMessage(session: WebSocketSession, response: IGameResponse) {
        if (!session.isOpen) {
            return
        }
        synchronized(lock) {
            println("---------------ACTION_TYPE ${response.action}")
            session.sendMessage(TextMessage(dtoToByteArray(response, objectMapper)))
        }
    }

    fun getPlayerName(session: WebSocketSession): String = session.attributes["name"].toString()
    fun getPlayerId(session: WebSocketSession): UUID? {
        return try {
            UUID.fromString(session.attributes["id"].toString())
        } catch (error: IllegalArgumentException) {
            null
        }
    }

    fun getGameId(session: WebSocketSession): UUID? {
        return try {
            UUID.fromString(session.attributes["gameId"].toString())
        } catch (error: IllegalArgumentException) {
            null
        }

    }

    fun getSessionId(session: WebSocketSession) = session.id
    fun getSessionByPlayerId(id: UUID) = activePlayers[id]
}