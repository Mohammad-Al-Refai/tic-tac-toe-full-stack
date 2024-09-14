
package com.mohammad.tic_tac_toe.services

import com.mohammad.tic_tac_toe.models.Board
import com.mohammad.tic_tac_toe.models.CellState
import com.mohammad.tic_tac_toe.models.Game
import com.mohammad.tic_tac_toe.repo.GameRepo
import com.mohammad.tic_tac_toe.responses.AvailableGames
import com.mohammad.tic_tac_toe.responses.GameCreated
import com.mohammad.tic_tac_toe.responses.GameDraw
import com.mohammad.tic_tac_toe.responses.JoinedGame
import com.mohammad.tic_tac_toe.responses.NewPlayerJoinedGame
import com.mohammad.tic_tac_toe.responses.PlayerQuitGame
import com.mohammad.tic_tac_toe.responses.UpdateGame
import com.mohammad.tic_tac_toe.responses.WinGame
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

@Service
class GameService(
    private val gameRepo: GameRepo,
    private var sessionService: SessionService,
) {
    val activeGames: MutableMap<UUID, Game> = mutableMapOf()

    suspend fun createGame(
        session: WebSocketSession,
        requestId: String,
    ) {
        val adminId = sessionService.getPlayerId(session) ?: return
        val game =
            Game(
                isPrivate = false,
                isDone = false,
                adminId = adminId,
                board = Board(emptyArray()).board,
                currentCellType = CellState.NONE,
                name = generateGameName(sessionService.getSessionId(session)),
            )
        val savedGame = gameRepo.save(game)
        activeGames[savedGame.id!!] = savedGame
        sessionService.sendMessage(session, GameCreated(game = savedGame, requestId = requestId))
    }

    suspend fun joinGame(
        session: WebSocketSession,
        gameId: UUID?,
        requestId: String,
    ) {
        if (gameId == null) {
            sessionService.sendError(session, "Missing gameId", requestId)
            return
        }
        if (isPlayerHasGames(session)) {
            sessionService.sendError(session, "You can't create another game", requestId)
            return
        }
        if (isPlayerInActiveGame(session)) {
            sessionService.sendError(session, "You already joined game", requestId)
            return
        }
        val game = getGame(gameId)
        if (game == null) {
            sessionService.sendError(session, "Game is not exist", requestId)
            return
        }
        if (game.isDone) {
            sessionService.sendError(session, "Game is done", requestId)
            return
        }
        if (game.playerId1 == null) {
            val id = sessionService.getPlayerId(session)
            game.apply {
                playerId1 = id
                player1Name = sessionService.getPlayerName(session)
                playerIdTurn = id
                currentCellType = CellState.X
            }
            val savedGame = gameRepo.save(game)
            activeGames[gameId] = savedGame
            sessionService.setGameId(session, gameId)
            sessionService.sendMessage(session, JoinedGame(game = savedGame, requestId = requestId))
        } else if (game.playerId2 == null) {
            game.apply {
                playerId2 = sessionService.getPlayerId(session)
                player2Name = sessionService.getPlayerName(session)
            }
            val savedGame = gameRepo.save(game)
            newPlayerJoinedNotification(
                game = savedGame,
                newPlayerId = savedGame.playerId2!!,
                newPlayerName = savedGame.player2Name!!,
                targetId = savedGame.playerId1!!,
            )
            activeGames[gameId] = savedGame
            sessionService.setGameId(session, gameId)
            sessionService.sendMessage(session, JoinedGame(game = game, requestId = requestId))
        } else {
            sessionService.sendError(session, "Game is full", requestId)
        }
    }

    suspend fun updateGame(
        session: WebSocketSession,
        requestId: String,
        gameId: UUID?,
        row: Int?,
        column: Int?,
    ) {
        if (gameId == null) {
            sessionService.sendError(session, "Missing gameId", requestId)
            return
        }
        if (row == null) {
            sessionService.sendError(session, "Missing row", requestId)
            return
        }
        if (column == null) {
            sessionService.sendError(session, "Missing column", requestId)
            return
        }

        if (sessionService.getGameId(session) != gameId) {
            sessionService.sendError(session, "You are not in the game", requestId)
            return
        }
        val localGame = activeGames[gameId]
        if (localGame == null) {
            sessionService.sendError(session, "Game is not exist", requestId)
            return
        }
        if (localGame.isDone) {
            sessionService.sendError(session, "Game is done", requestId)
            return
        }
        if (localGame.playerIdTurn != sessionService.getPlayerId(session)) {
            sessionService.sendError(session, "It's not your turn yet", requestId)
            return
        }
        var localBoard = Board(localGame.board)
        if (!localBoard.update(row, column, localGame.currentCellType)) {
            sessionService.sendError(session, "Is filled", requestId)
            return
        }
        localGame.apply {
            board = localBoard.get()
            playerIdTurn = getPlayerTurn(localGame)
            currentCellType = getCellTurn(localGame.currentCellType)
        }
        val savedGame = gameRepo.save(localGame)
        val response =
            UpdateGame(
                gameId = savedGame.id!!,
                playerIdTurn = savedGame.playerIdTurn!!,
                board = savedGame.board,
                turn = savedGame.currentCellType,
                requestId = requestId,
            )
        activeGames[gameId] = savedGame
        val newState = activeGames[gameId]
        localBoard = Board(newState?.board!!)
        sessionService.getSessionByPlayerId(localGame.playerId1!!).let {
            if (it != null) sessionService.sendMessage(it, response)
        }
        sessionService.getSessionByPlayerId(localGame.playerId2!!).let {
            if (it != null) sessionService.sendMessage(it, response)
        }
        if (localBoard.isWin() == CellState.X) {
            sessionService.getSessionByPlayerId(localGame.playerId1!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        WinGame(gameId = localGame.id!!, playerId = localGame.playerId1!!, winner = CellState.X),
                    )
                }
            }
            sessionService.getSessionByPlayerId(localGame.playerId2!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        WinGame(gameId = localGame.id!!, playerId = localGame.playerId1!!, winner = CellState.X),
                    )
                }
            }
            localGame.apply {
                isDone = true
            }

            activeGames[localGame.id!!] = gameRepo.save(localGame)
            return
        }
        if (localBoard.isWin() == CellState.O) {
            sessionService.getSessionByPlayerId(localGame.playerId1!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        WinGame(gameId = localGame.id!!, playerId = localGame.playerId2!!, winner = CellState.O),
                    )
                }
            }
            sessionService.getSessionByPlayerId(localGame.playerId2!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        WinGame(gameId = localGame.id!!, playerId = localGame.playerId2!!, winner = CellState.O),
                    )
                }
            }
            localGame.apply {
                isDone = true
            }
            activeGames[localGame.id!!] = gameRepo.save(localGame)
            return
        }
        if (localBoard.isDraw()) {
            sessionService.getSessionByPlayerId(localGame.playerId1!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        GameDraw(),
                    )
                }
            }
            sessionService.getSessionByPlayerId(localGame.playerId2!!).let {
                if (it != null) {
                    sessionService.sendMessage(
                        it,
                        GameDraw(),
                    )
                }
            }
            localGame.apply {
                isDone = true
            }
            activeGames[localGame.id!!] = gameRepo.save(localGame)
        }
    }

    suspend fun isPlayerHasGames(session: WebSocketSession): Boolean {
        val playerId = sessionService.getPlayerId(session) ?: return false
        val playerAsFirstGames = gameRepo.findGamesByPlayerId1(playerId)
        val playerAsSecondGames = gameRepo.findGamesByPlayerId2(playerId)
        return playerAsFirstGames.isNotEmpty() && playerAsSecondGames.isNotEmpty()
    }

    suspend fun isPlayerInActiveGame(session: WebSocketSession): Boolean {
        val id = sessionService.getPlayerId(session) ?: return false
        return gameRepo.findUnfinishedGamesForPlayerId(id).isNotEmpty()
    }

    suspend fun newPlayerJoinedNotification(
        game: Game,
        targetId: UUID,
        newPlayerId: UUID,
        newPlayerName: String,
    ) {
        val otherSession = sessionService.getSessionByPlayerId(targetId) ?: return
        if (!otherSession.isOpen) {
            return
        }
        sessionService.sendMessage(
            otherSession,
            NewPlayerJoinedGame(
                game = game,
                playerId = newPlayerId,
                playerName = newPlayerName,
            ),
        )
    }

    suspend fun playerQuitNotification(session: WebSocketSession) {
        val gameId = sessionService.getGameId(session) ?: return
        val game = activeGames[sessionService.getGameId(session)] ?: return
        if (game.isDone) {
            return
        }
        val player1Session = sessionService.getSessionByPlayerId(game.playerId1!!) ?: return
        val player2Session = sessionService.getSessionByPlayerId(game.playerId2!!) ?: return
        val player1Id = sessionService.getPlayerId(player1Session) ?: return
        if (game.playerId1 == sessionService.getPlayerId(session)) {
            sessionService.sendMessage(
                player2Session,
                PlayerQuitGame(
                    gameId = gameId,
                    playerId = player1Id,
                    playerName = sessionService.getPlayerName(player1Session),
                ),
            )
            return
        }
        if (game.playerId2 == sessionService.getPlayerId(session)) {
            val player2Id = sessionService.getPlayerId(player2Session) ?: return
            sessionService.sendMessage(
                player1Session,
                PlayerQuitGame(
                    gameId = gameId,
                    playerId = player2Id,
                    playerName = sessionService.getPlayerName(player2Session),
                ),
            )
        }
    }

    suspend fun getAvailableGames(
        session: WebSocketSession,
        requestId: String,
    ) = sessionService.sendMessage(
        session,
        AvailableGames(games = gameRepo.findAvailableGames(), requestId = requestId),
    )

    suspend fun getGame(gameId: UUID?): Game? {
        if (gameId == null) {
            return null
        }
        return gameRepo.findById(gameId)
    }

    suspend fun deleteGame(session: WebSocketSession) {
        val gameId = sessionService.getGameId(session) ?: return
        gameRepo.deleteById(gameId)
        activeGames.remove(gameId)
    }

    suspend fun quitGame(session: WebSocketSession) {
        playerQuitNotification(session)
        deleteGame(session)
    }

    private fun getCellTurn(currentCellType: CellState): CellState {
        if (currentCellType == CellState.NONE) {
            return CellState.X
        }
        return if (currentCellType == CellState.X) CellState.O else CellState.X
    }

    private fun getPlayerTurn(game: Game): UUID? {
        if (game.playerId1 == game.playerIdTurn) {
            return game.playerId2
        }
        if (game.playerId2 == game.playerIdTurn) {
            return game.playerId1
        }
        return null
    }

    private fun generateGameName(id: String): String = "game_${id.substring(0, 5)}"
}
