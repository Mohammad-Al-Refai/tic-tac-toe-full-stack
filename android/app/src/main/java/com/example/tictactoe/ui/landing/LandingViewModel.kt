package com.example.tictactoe.ui.landing

import androidx.compose.material3.Snackbar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LandingViewModel(
    private val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val gameState: StateFlow<GameState>
) : ViewModel() {
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> get() = _snackbarEvent

    init {
        viewModelScope.launch {
            getAvailableGamesEvery5Seconds()
            gameState.collect {
                if (it.error != null) {
                    _snackbarEvent.emit(it.error!!)
                }
            }
        }
    }

    fun navigateToPlay() {
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