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
import com.example.tictactoe.ui.NavHost.NavGraph
import com.example.tictactoe.ui.loading.LoadingPage
import com.example.tictactoe.ui.theme.TicTacToeTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main(vm: AppViewModel = koinInject()) {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    val state = vm.appState.collectAsState()
    LaunchedEffect(Unit) {
        vm.connect()
        vm.listenForServiceMessages()
        vm.listenForSnackBarMessages(snackBarScope, snackBarHostState)
    }

    if (!state.value.isConnected || state.value.isConnectionError) {
        LoadingPage(vm.appState) {
            vm.connect()
        }
    }
    if (state.value.isConnected) {
        NavGraph(
            navController = navController,
            vm.appState,
            vm.ticTacToeService,
            vm.snackBarEvent,
            snackBarHostState,
        )
    }
}
