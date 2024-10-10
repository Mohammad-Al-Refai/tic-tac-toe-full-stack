package com.example.tictactoe.models

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(
    val action: ActionResponse = ActionResponse.NONE,
    val requestId: String = "",
    val game: Game? = null,
    val games: List<Game>? = null,
    var errorMessage: String? = null,
    var playerId: String? = null,
    var playerName: String? = null,
    var clientId: String? = null,
    var name: String? = null,
    var gameId: String? = null,
    var playerIdTurn: String? = null,
    val turn: CellState? = null,
    val board: Array<Array<String>>? = null,
    var winner: CellState? = null,
)