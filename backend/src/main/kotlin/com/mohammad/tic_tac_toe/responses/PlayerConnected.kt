package com.mohammad.tic_tac_toe.responses


import java.util.*

data class PlayerConnected(
    override var action: ActionResponse = ActionResponse.CONNECTED,
    var clientId: UUID? = null,
    var name: String
) : IGameResponse