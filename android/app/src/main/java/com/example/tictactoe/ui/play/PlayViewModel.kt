package com.example.tictactoe.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.tictactoe.models.AppState
import com.example.tictactoe.network.TicTacToeService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class PlayViewModel(
    private val navHostController: NavHostController,
    private val ticTacToeService: TicTacToeService,
    val appState: StateFlow<AppState>,
    private val snackBarEvent: MutableSharedFlow<String>,
) : ViewModel() {
    private val _state = MutableStateFlow<ConfettiState>(ConfettiState.Idle)
    val state: MutableStateFlow<ConfettiState> = _state

    init {
        viewModelScope.launch {
            appState.value.isGameStarted = true
            appState.collect {
                if (it.isGameFinished && it.isWinCurrentGame) {
                    startConfetti()
                }
                if (it.isGameStarted && it.isOpponentQuitGame) {
                    snackBarEvent.emit("${it.opponent.opponentName} quit game")
                    ticTacToeService.resetAvailableGames()
                    ticTacToeService.resetGameState()
                    navHostController.popBackStack()
                }
            }
        }
    }

    fun onCellClick(
        row: Int,
        col: Int,
    ) {
        viewModelScope.launch {
            ticTacToeService.updateGame(row, col)
        }
    }

    fun getTurnText(appState: AppState): String =
        if (appState.playIdTurn == appState.clientId) {
            "Your turn"
        } else {
            "${appState.opponent.opponentName} turn"
        }

    fun getGameStatusText(appState: AppState): String =
        if (appState.isGameFinished && appState.isWinCurrentGame) {
            "You Win"
        } else {
            "You Lost"
        }

    fun quitGame() {
        viewModelScope.launch {
            ticTacToeService.quitGame()
            navHostController.popBackStack()
        }
    }

    private fun startConfetti() {
        val party =
            Party(
                speed = 10f,
                maxSpeed = 30f,
                damping = 0.9f,
                angle = Angle.RIGHT - 45,
                spread = Spread.SMALL,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
                position = Position.Relative(0.0, 0.5),
            )
        _state.value =
            ConfettiState.Started(
                mutableListOf(
                    party,
                    party.copy(
                        angle = party.angle - 90, // flip angle from right to left
                        position = Position.Relative(1.0, 0.5),
                    ),
                ),
            )
    }

    fun ended() {
        _state.value = ConfettiState.Idle
    }

    sealed class ConfettiState {
        class Started(
            val party: List<Party>,
        ) : ConfettiState()

        object Idle : ConfettiState()
    }
}
