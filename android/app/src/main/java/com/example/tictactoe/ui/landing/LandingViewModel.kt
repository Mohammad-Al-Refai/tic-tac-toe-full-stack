package com.example.tictactoe.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LandingViewModel(
    val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val gameState: StateFlow<GameState>,
    val snackBarEvent: MutableSharedFlow<String>,
) : ViewModel() {
    var availableGamesJob: Job? = null

    init {
        ticTacToeService.resetGameState()
        viewModelScope.launch {
            startGetAvailableGames()
            gameState.collect {
                if (it.isJoinedGame && !it.isGameStarted) {
                    stopGetAvailableGames()
                    navigateToPlay()
                }
                if (it.error != null) {
                    println("------ERROR: ${it.error}")
                    snackBarEvent.emit(it.error!!)
                    gameState.value.error = null
                }
            }
        }
    }

    fun joinGame(game: Game) {
        if (game.id.isNullOrEmpty()) {
            viewModelScope.launch {
                snackBarEvent.emit("Game id is unavailable")
            }
            return
        }
        viewModelScope.launch {
            ticTacToeService.joinGame(game.id)
        }
    }

    private fun navigateToPlay() {
        navHostController.navigate(Routes.Play.name)
    }

    fun startGetAvailableGames() {
        if (availableGamesJob?.isActive == true) return
        availableGamesJob =
            viewModelScope.launch {
                while (true) {
                    ticTacToeService.getAvailableGames()
                    delay(5000L)
                }
            }
    }

    fun stopGetAvailableGames() {
        availableGamesJob?.cancel()
    }
}
