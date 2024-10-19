package com.example.tictactoe.models

import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponse(
    val action: ActionResponse = ActionResponse.NONE,
    val requestId: String? = null,
    val games: List<Game>? = null,
    var errorMessage: String? = null,
    var playerId: String? = null,
    var playerName: String? = null,
    var clientId: String? = null,
    var name: String? = null,
    var gameId: String? = null,
    var playerIdTurn: String? = null,
    var winner: CellState? = null,
    var turn: CellState = CellState.NONE,
    var game: Game? = null,
    var board: Array<Array<CellState>> = arrayOf(),
)
