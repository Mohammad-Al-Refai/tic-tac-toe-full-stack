package com.mohammad.tic_tac_toe.responses


data class GameDraw(
    override var action: ActionResponse = ActionResponse.DRAW,
) : IGameResponse