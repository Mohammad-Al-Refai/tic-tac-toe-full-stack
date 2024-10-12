package com.example.tictactoe.models

data class GameState(
    var availableGames: List<Game> = emptyList(),
    var clientId: String? = null,
    var isConnected: Boolean = false,
    var isLoading: Boolean = false,
    var isGetAvailableGamesLoading: Boolean = false,
    var isConnectionError: Boolean = false,
    var error: String? = null,
    var isGameReady: Boolean = false,
    var clientName: String = "",
    var turn: CellState? = null,
    var playIdTurn: String? = null,
    var opponent: Opponent = Opponent("", ""),
    var board: Array<Array<CellState>> = arrayOf(),
    var gameId: String? = null,
    var myCellState: CellState = CellState.NONE
)