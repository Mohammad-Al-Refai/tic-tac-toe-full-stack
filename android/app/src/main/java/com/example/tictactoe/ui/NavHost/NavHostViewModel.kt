package com.example.tictactoe.ui.NavHost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.network.TicTacToeService
import kotlinx.coroutines.launch

class NavHostViewModel(val ticTacToeService: TicTacToeService) : ViewModel() {

    init {
        ticTacToeService.connect()
    }

    override fun onCleared() {
        println("NavHostViewModel CLEARED")
        viewModelScope.launch {
            ticTacToeService.close()
        }
    }
}