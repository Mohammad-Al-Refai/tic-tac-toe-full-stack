package com.example.tictactoe.ui.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tictactoe.models.GameState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoadingPage(
    gameState: StateFlow<GameState>,
    connect: () -> Unit,
) {
    val state = gameState.collectAsState()
    Scaffold { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator()
                Text(text = "Connecting...")
            }
            if (state.value.isConnectionError) {
                Text(text = "Field to connect to server")
                Button(onClick = connect) {
                    Text(text = "Try again")
                }
            }
        }
    }
}
