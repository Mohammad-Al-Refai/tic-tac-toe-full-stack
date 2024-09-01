package com.mohammad.tec.tac_toe.models

import java.util.*

enum class ActionResponse {
    NONE,
    ERROR,
    CONNECTED,
    GAME_CREATED,
    JOINED_GAME,
    NEW_PLAYER_JOINED,
    UPDATE_GAME,
    PLAYER_QUIT,
    AVAILABLE_GAMES,
    WIN,
    DRAW
}

interface IGameResponse {
    val action: ActionResponse
}

data class PlayerConnected(
    override var action: ActionResponse = ActionResponse.CONNECTED,
    var clientId: UUID? = null,
    var name: String
) : IGameResponse

data class GameError(
    override var action: ActionResponse = ActionResponse.ERROR,
    var errorMessage: String = ""
) : IGameResponse

data class GameCreated(
    override var action: ActionResponse = ActionResponse.GAME_CREATED,
    var game: Game
) : IGameResponse

data class GameDraw(
    override var action: ActionResponse = ActionResponse.DRAW,
) : IGameResponse

data class JoinedGame(
    override var action: ActionResponse = ActionResponse.JOINED_GAME,
    val game: Game
) : IGameResponse

data class NewPlayerJoinedGame(
    override var action: ActionResponse = ActionResponse.NEW_PLAYER_JOINED,
    var game: Game,
    var playerId: UUID,
    var playerName: String
) : IGameResponse

data class PlayerQuitGame(
    override var action: ActionResponse = ActionResponse.PLAYER_QUIT,
    var gameId: UUID,
    var playerId: UUID,
    var playerName: String
) : IGameResponse

data class UpdateGame(
    override var action: ActionResponse = ActionResponse.UPDATE_GAME,
    var gameId: UUID,
    var playerIdTurn: UUID,
    val turn: CellState,
    val board: Array<Array<String>>
) : IGameResponse

data class WinGame(
    override var action: ActionResponse = ActionResponse.WIN,
    var gameId: UUID,
    var winner: CellState,
    var playerId: UUID
) : IGameResponse

data class AvailableGames(
    override var action: ActionResponse = ActionResponse.AVAILABLE_GAMES,
    var games: Collection<AvailableGamesItem>
) : IGameResponse
data class AvailableGamesItem(
    val id:UUID,
    val name:String
)