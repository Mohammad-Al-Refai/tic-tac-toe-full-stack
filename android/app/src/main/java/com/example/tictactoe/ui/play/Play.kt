package com.example.tictactoe.ui.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.components.CustomToolBar
import com.example.tictactoe.ui.theme.Typography
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem

@Composable
fun Play(
    vm: PlayViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val state = vm.appState.collectAsState()
    val animationState = vm.state.collectAsState()

    Scaffold(
        topBar = {
            CustomToolBar(state.value.clientName)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = "Home Icon",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Text("You", style = Typography.bodyLarge)
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Rounded.AccountCircle,
                        contentDescription = "Home Icon",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                    Text(state.value.opponent.opponentName)
                }
            }
            if (!state.value.isGameFinished) {
                Text(vm.getTurnText(state.value))
            } else {
                Text(vm.getGameStatusText(state.value))
            }
            Board(state.value.board, state.value.myCellState, onCellClick = vm::onCellClick)
            Column {
                Button(onClick = vm::quitGame) {
                    Text("Quit game")
                }
            }
        }
        when (animationState.value) {
            is PlayViewModel.ConfettiState.Started ->
                KonfettiView(
                    modifier = Modifier.fillMaxSize(),
                    parties = (animationState.value as PlayViewModel.ConfettiState.Started).party,
                    updateListener =
                        object : OnParticleSystemUpdateListener {
                            override fun onParticleSystemEnded(
                                system: PartySystem,
                                activeSystems: Int,
                            ) {
                                if (activeSystems == 0) vm.ended()
                            }
                        },
                )

            PlayViewModel.ConfettiState.Idle -> null
        }
    }
}
