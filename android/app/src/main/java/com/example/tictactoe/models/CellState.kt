package com.example.tictactoe.models

import kotlinx.serialization.Serializable

@Serializable
enum class CellState {
    NONE,
    X,
    O
}