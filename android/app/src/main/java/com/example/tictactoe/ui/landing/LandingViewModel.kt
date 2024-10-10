package com.example.tictactoe.ui.landing

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.delay
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
    init {
        viewModelScope.launch {
            println("----LandingViewModel init: ${ticTacToeService.webSocketSession!!.isActive}")
        }
    }

    fun navigateToPlay() {
        navHostController.navigate(Routes.Play.name)
    }

    fun getAvailableGamesEvery5Seconds() {
        viewModelScope.launch {
            while (true) {
                println("----Calling getAvailableGames")
                ticTacToeService.getAvailableGames()
                delay(5000L)
            }
        }

    }
}