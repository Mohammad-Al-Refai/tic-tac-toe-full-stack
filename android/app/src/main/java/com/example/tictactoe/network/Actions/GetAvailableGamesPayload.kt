package com.example.tictactoe.network.Actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class GetAvailableGamesPayload(
    val action: ActionRequest = ActionRequest.GET_AVAILABLE_GAMES,
    val clientId: String,
    val requestId: String = UUID.randomUUID().toString()
)