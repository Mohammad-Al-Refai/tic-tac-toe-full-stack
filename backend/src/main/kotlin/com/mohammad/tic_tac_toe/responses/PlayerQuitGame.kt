package com.mohammad.tic_tac_toe.responses

import java.util.*

data class PlayerQuitGame(
    override var action: ActionResponse = ActionResponse.PLAYER_QUIT,
    var gameId: UUID,
    var playerId: UUID,
    var playerName: String
) : IGameResponse