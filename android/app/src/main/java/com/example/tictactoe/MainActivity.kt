package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.NavHost.NavGraph
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ticTacToeService: TicTacToeService by inject()
        val gameState = ticTacToeService.gameState
        lifecycleScope.launch {
            ticTacToeService.connect()
        }
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Game(gameState, ticTacToeService)
            }
        }
    }
}

@Composable
fun Game(
    gameState: StateFlow<GameState>,
    ticTacToeService: TicTacToeService,
) {
    val navController = rememberNavController()
    NavGraph(navController = navController, gameState, ticTacToeService)
}
