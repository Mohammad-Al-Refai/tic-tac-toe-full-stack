package com.example.tictactoe.network.actions

import com.example.tictactoe.models.ActionRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateGamePayload(
    val action: ActionRequest = ActionRequest.CREATE_GAME,
    val clientId: String,
    val requestId: String = UUID.randomUUID().toString(),
)
