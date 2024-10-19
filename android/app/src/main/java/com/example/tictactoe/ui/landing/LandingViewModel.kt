package com.example.tictactoe.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.AppState
import com.example.tictactoe.models.Game
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import com.example.tictactoe.ui.utils.GET_AVAILABLE_GAMES_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LandingViewModel(
    val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val appState: StateFlow<AppState>,
) : ViewModel() {
    private var availableGamesJob: Job? = null

    init {
        viewModelScope.launch {
            startGetAvailableGames()
            appState.collect {
                if (it.isJoinedGame && !it.isGameStarted) {
                    stopGetAvailableGames()

                    navigateToPlay()
                }
                if (it.isConnectionError) {
                    stopGetAvailableGames()
                }
            }
        }
    }

    fun joinGame(game: Game) {
        viewModelScope.launch {
            ticTacToeService.joinGame(game.id!!)
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
                    delay(GET_AVAILABLE_GAMES_DELAY)
                }
            }
    }

    fun stopGetAvailableGames() {
        availableGamesJob?.cancel()
    }
}
