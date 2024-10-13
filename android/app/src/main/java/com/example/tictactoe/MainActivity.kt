package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.NavHost.NavGraph
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarEvent = MutableSharedFlow<String>()
    val snackBarScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        snackBarEvent.collectLatest { message ->
            snackBarScope.launch {
                snackBarHostState.showSnackbar(message)
            }
        }
    }
    NavGraph(navController = navController, gameState, ticTacToeService, snackBarEvent, snackBarHostState)
}
