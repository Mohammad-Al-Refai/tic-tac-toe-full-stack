package com.mohammad.tic_tac_toe.responses

import com.mohammad.tec.tac_toe.models.Game
import java.util.*


data class NewPlayerJoinedGame(
    override var action: ActionResponse = ActionResponse.NEW_PLAYER_JOINED,
    var game: Game,
    var playerId: UUID,
    var playerName: String
) : IGameResponse
