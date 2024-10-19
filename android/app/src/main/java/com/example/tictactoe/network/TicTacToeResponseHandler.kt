package com.example.tictactoe.network

import com.example.tictactoe.models.ActionResponse
import com.example.tictactoe.models.AppState
import com.example.tictactoe.models.CellState
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.Opponent
import com.example.tictactoe.models.ServiceResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun handleResponse(
    response: ServiceResponse,
    appState: MutableStateFlow<AppState>,
) {
    println("---Response: $response")
    when (response.action) {
        ActionResponse.CONNECTED -> {
            appState.update {
                it.copy(
                    clientId = response.clientId,
                    isConnected = true,
                    isLoading = false,
                    clientName = response.name!!,
                )
            }
        }

        ActionResponse.AVAILABLE_GAMES -> {
            appState.update {
                it.copy(
                    availableGames = response.games.orEmpty(),
                    isGetAvailableGamesLoading = false,
                )
            }
        }

        ActionResponse.NONE -> TODO()
        ActionResponse.ERROR -> {
            appState.update {
                it.copy(error = response.errorMessage)
            }
        }

        ActionResponse.GAME_CREATED -> TODO()
        ActionResponse.JOINED_GAME -> {
            appState.update {
                it.copy(
                    isJoinedGame = true,
                    isJoiningGame = false,
                    gameId = response.game!!.id,
                    board = response.game!!.board,
                    opponent = getOpponent(response.game!!, it.clientId!!),
                    myCellState = getMyCellState(response.game!!, it.clientId!!),
                )
            }
        }

        ActionResponse.NEW_PLAYER_JOINED -> TODO()
        ActionResponse.UPDATE_GAME -> {
            appState.update {
                it.copy(
                    playIdTurn = response.playerIdTurn,
                    turn = response.turn,
                    board = response.board,
                )
            }
        }

        ActionResponse.PLAYER_QUIT -> {
            appState.update {
                it.copy(
                    isOpponentQuitGame = true,
                )
            }
        }

        ActionResponse.WIN -> {
            appState.update {
                it.copy(
                    isGameFinished = true,
                    isWinCurrentGame = response.winner == it.myCellState,
                    isLoseCurrentGame = response.winner != it.myCellState,
                )
            }
        }

        ActionResponse.DRAW -> TODO()
    }
}

private fun getOpponent(
    game: Game,
    clientId: String,
): Opponent {
    var name = ""
    var id = ""
    if (game.playerId1 != clientId) {
        name = game.player1Name!!
        id = game.playerId1!!
    }
    if (game.playerId2 != clientId) {
        name = game.player2Name!!
        id = game.playerId2!!
    }
    return Opponent(name, id)
}

private fun getMyCellState(
    game: Game,
    clientId: String,
): CellState {
    if (game.playerId1 == clientId) {
        return CellState.X
    }
    if (game.playerId2 == clientId) {
        return CellState.O
    }
    return CellState.NONE
}
