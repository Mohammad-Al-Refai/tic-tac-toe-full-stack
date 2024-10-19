package com.example.tictactoe

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.example.tictactoe.network.TicTacToeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppViewModel(
    val ticTacToeService: TicTacToeService,
) : ViewModel() {
    private val job = Job()
    private val connectionScope = CoroutineScope(job)
    private val messagesScope = CoroutineScope(job)
    val snackBarEvent = MutableSharedFlow<String>()
    val appState = ticTacToeService.appState

    fun connect() {
        connectionScope.launch {
            ticTacToeService.connect()
        }
    }

    fun listenForSnackBarMessages(
        snackBarScope: CoroutineScope,
        snackBarHostState: SnackbarHostState,
    ) {
        messagesScope.launch {
            snackBarEvent.collectLatest { message ->
                snackBarScope.launch {
                    snackBarHostState.showSnackbar(message)
                }
            }
        }
    }

    fun listenForServiceMessages() {
        messagesScope.launch {
            appState.collect {
                if (it.error != null) {
                    snackBarEvent.emit(it.error!!)
                    ticTacToeService.resetError()
                }
                if (it.isGameStarted && it.isOpponentQuitGame) {
                    snackBarEvent.emit("${it.opponent.opponentName} quit game")
                }
            }
        }
    }
}
