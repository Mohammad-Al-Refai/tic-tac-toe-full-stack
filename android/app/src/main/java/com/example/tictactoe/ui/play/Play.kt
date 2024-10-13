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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem

@Composable
fun Play(vm: PlayViewModel) {
    val gameState = vm.gameState.collectAsState()
    val animationState = vm.state.collectAsState()

    Scaffold { innerPadding ->
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
                    Text("You")
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
                    Text(gameState.value.opponent.opponentName)
                }
            }
            if (!gameState.value.isGameFinished) {
                Text(vm.getTurnText(gameState.value))
            } else {
                Text(vm.getGameStatusText(gameState.value))
            }
            Board(gameState.value.board, gameState.value.myCellState, onCellClick = vm::onCellClick)
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
