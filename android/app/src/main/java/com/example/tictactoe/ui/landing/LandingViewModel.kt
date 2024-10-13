package com.example.tictactoe.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LandingViewModel(
    private val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val gameState: StateFlow<GameState>,
) : ViewModel() {
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> get() = _snackbarEvent

    init {
        ticTacToeService.resetGameState()
        viewModelScope.launch {
            if (!gameState.value.isJoinedGame) {
                getAvailableGamesEvery5Seconds()
            }
            gameState.collect {
                if (it.isJoinedGame && !it.isGameStarted) {
                    navigateToPlay()
                }
                if (it.error != null) {
                    println("------ERROR: ${it.error}")
                    _snackbarEvent.emit(it.error!!)
                }
            }
        }
    }

    private fun navigateToPlay() {
        navHostController.navigate(Routes.Play.name)
    }

    fun joinGame(game: Game) {
        if (game.id.isNullOrEmpty()) {
            viewModelScope.launch {
                _snackbarEvent.emit("Game id is unavailable")
            }
            return
        }
        viewModelScope.launch {
            ticTacToeService.joinGame(game.id)
        }
    }

    private suspend fun getAvailableGamesEvery5Seconds() {
        viewModelScope.launch {
            while (true) {
                println("----Calling getAvailableGames")
                ticTacToeService.getAvailableGames()
                delay(5000L)
            }
        }
    }
}
