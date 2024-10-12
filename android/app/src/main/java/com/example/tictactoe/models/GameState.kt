package com.example.tictactoe.models

data class GameState(
    var availableGames: List<Game> = emptyList(),
    var clientId: String? = null,
    var isConnected: Boolean = false,
    var isLoading: Boolean = false,
    var isGetAvailableGamesLoading: Boolean = false,
    var isConnectionError: Boolean = false,
    var error: String? = null,
)