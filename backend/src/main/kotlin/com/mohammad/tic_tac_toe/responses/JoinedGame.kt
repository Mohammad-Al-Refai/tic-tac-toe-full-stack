package com.mohammad.tic_tac_toe.responses

import com.mohammad.tec.tac_toe.models.Game

data class JoinedGame(
    val requestId: String,
    override var action: ActionResponse = ActionResponse.JOINED_GAME,
    val game: Game
) : IGameResponse