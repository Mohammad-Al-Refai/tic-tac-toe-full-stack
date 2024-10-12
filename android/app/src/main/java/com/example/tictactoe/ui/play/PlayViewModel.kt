package com.example.tictactoe.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayViewModel(
    private val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val gameState: StateFlow<GameState>,
) : ViewModel() {
    fun onCellClick(row: Int, col: Int) {
        viewModelScope.launch {
            ticTacToeService.updateGame(row, col)
        }
    }
}