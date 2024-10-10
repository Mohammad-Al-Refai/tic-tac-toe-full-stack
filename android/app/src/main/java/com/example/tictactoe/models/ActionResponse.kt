package com.example.tictactoe.models

enum class ActionResponse {
    NONE,
    ERROR,
    CONNECTED,
    GAME_CREATED,
    JOINED_GAME,
    NEW_PLAYER_JOINED,
    UPDATE_GAME,
    PLAYER_QUIT,
    AVAILABLE_GAMES,
    WIN,
    DRAW
}