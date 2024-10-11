package com.example.tictactoe.models

import kotlinx.serialization.Serializable

@Serializable
enum class ActionRequest {
    CREATE_GAME,
    JOIN_GAME,
    UPDATE_GAME,
    GET_AVAILABLE_GAMES,
    QUIT_GAME,
}

