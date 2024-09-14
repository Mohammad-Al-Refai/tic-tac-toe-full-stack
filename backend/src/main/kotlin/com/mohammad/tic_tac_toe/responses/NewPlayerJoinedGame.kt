package com.mohammad.tic_tac_toe.responses

import com.mohammad.tic_tac_toe.models.Game
import java.util.UUID

data class NewPlayerJoinedGame(
    override var action: ActionResponse = ActionResponse.NEW_PLAYER_JOINED,
    var game: Game,
    var playerId: UUID,
    var playerName: String,
) : IGameResponse
