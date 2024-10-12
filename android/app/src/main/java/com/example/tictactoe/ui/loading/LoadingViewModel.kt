package com.example.tictactoe.ui.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.ui.Routes
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoadingViewModel(
    private val navHostController: NavHostController,
    val gameState: StateFlow<GameState>,
) : ViewModel() {


    init {
        viewModelScope.launch {
            gameState.collect {
                if (it.isConnected) {
                    navigateToLanding()
                    viewModelScope.cancel()
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
//        ticTacToeService.connect()
    }
}
