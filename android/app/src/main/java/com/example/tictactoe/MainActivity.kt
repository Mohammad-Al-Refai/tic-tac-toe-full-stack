package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.NavHost.NavGraph
import com.example.tictactoe.ui.loading.LoadingPage
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Game()
            }
        }
    }
}

@Composable
fun Game() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarEvent = MutableSharedFlow<String>()
    val snackBarScope = rememberCoroutineScope()
    val ticTacToeService: TicTacToeService = koinInject()
    val gameState = ticTacToeService.gameState
    val state = gameState.collectAsState()
    val job = Job()
    val scope = CoroutineScope(job)
    LaunchedEffect(Unit) {
        scope.launch {
            ticTacToeService.connect()
        }
        snackBarEvent.collectLatest { message ->
            snackBarEvent.emit(message)
        }
    }

    if (!state.value.isConnected || state.value.isConnectionError) {
        LoadingPage(gameState) {
            scope.launch {
                ticTacToeService.connect()
            }
        }
    }
    if (state.value.isConnected) {
        NavGraph(
            navController = navController,
            gameState,
            ticTacToeService,
            snackBarEvent,
            snackBarHostState,
        )
    }
}
