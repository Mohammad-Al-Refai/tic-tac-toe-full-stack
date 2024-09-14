package com.mohammad.tic_tac_toe.responses

import com.mohammad.tic_tac_toe.models.CellState
import java.util.*

data class WinGame(
    override var action: ActionResponse = ActionResponse.WIN,
    var gameId: UUID,
    var winner: CellState,
    var playerId: UUID
) : IGameResponse