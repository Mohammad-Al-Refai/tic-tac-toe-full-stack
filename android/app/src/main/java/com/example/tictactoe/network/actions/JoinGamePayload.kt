package com.example.tictactoe.network.actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class JoinGamePayload(
    val action: ActionRequest,
    val clientId: String,
    val requestId: String = UUID.randomUUID().toString(),
    val gameId: String,
)