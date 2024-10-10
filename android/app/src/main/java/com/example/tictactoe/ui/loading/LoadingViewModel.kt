package com.example.tictactoe.ui.loading

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.ActionResponse
import com.example.tictactoe.models.GameResponse
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoadingViewModel(
    private val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val gameState: StateFlow<GameState>,
) : ViewModel() {

    init {
        viewModelScope.launch {
            gameState.collect {
                if (it.isConnected) {
                    navigateToLanding()
                }
            }
        }
    }

    private fun navigateToLanding() {
        navHostController.popBackStack()
        navHostController.navigate(Routes.Landing.name)
    }

    fun connect() {
        println("---Connecting")
        ticTacToeService.connect()

    }
}
