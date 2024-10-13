package com.example.tictactoe.network.actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GetAvailableGamesPayload(
    val action: ActionRequest,
    val clientId: String,
    val requestId: String = UUID.randomUUID().toString(),
)
