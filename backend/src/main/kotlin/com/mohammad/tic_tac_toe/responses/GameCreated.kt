package com.mohammad.tic_tac_toe.responses

import com.mohammad.tic_tac_toe.models.Game


data class GameCreated(
    val requestId: String,
    override var action: ActionResponse = ActionResponse.GAME_CREATED,
    var game: Game
) : IGameResponse