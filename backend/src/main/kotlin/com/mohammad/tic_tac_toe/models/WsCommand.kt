package com.mohammad.tic_tac_toe.models

import java.util.*

enum class ActionType {
    CREATE_GAME,
    JOIN_GAME,
    UPDATE_GAME,
    GET_AVAILABLE_GAMES,
    QUIT_GAME,
}

data class WsCommand(
    val action: ActionType,
    val gameId: UUID?,
    val clientId: UUID?,
    val board: Array<Array<String>>?,
    val isGamePrivate: Boolean?,
    val row: Int?,
    val column: Int?,
    val requestId: String,
)
