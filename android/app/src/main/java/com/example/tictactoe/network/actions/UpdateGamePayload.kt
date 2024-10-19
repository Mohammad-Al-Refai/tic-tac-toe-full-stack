package com.example.tictactoe.network.actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UpdateGamePayload(
    val action: ActionRequest,
    val requestId: String = UUID.randomUUID().toString(),
    val clientId: String,
    val gameId: String,
    val row: Int,
    val column: Int,
)
