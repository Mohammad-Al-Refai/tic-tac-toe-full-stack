package com.example.tictactoe.network.actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class QuitGamePayload(
    val action: ActionRequest,
    val requestId: String = UUID.randomUUID().toString(),
    val gameId: String,
    val clientId: String,
)
