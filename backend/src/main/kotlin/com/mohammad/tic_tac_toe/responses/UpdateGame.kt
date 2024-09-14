package com.mohammad.tic_tac_toe.responses

import com.mohammad.tic_tac_toe.models.CellState
import java.util.*

data class UpdateGame(
    val requestId: String,
    override var action: ActionResponse = ActionResponse.UPDATE_GAME,
    var gameId: UUID,
    var playerIdTurn: UUID,
    val turn: CellState,
    val board: Array<Array<String>>
) : IGameResponse