package com.mohammad.tic_tac_toe.responses


import java.util.UUID

data class AvailableGames(
    val requestId: String,
    override var action: ActionResponse = ActionResponse.AVAILABLE_GAMES,
    var games: Collection<AvailableGamesItem>
) : IGameResponse

data class AvailableGamesItem(
    val id: UUID,
    val name: String
)