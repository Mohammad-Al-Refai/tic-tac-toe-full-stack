package com.mohammad.tic_tac_toe.responses


data class GameError(
    val requestId: String,
    override var action: ActionResponse = ActionResponse.ERROR,
    var errorMessage: String = ""
) : IGameResponse