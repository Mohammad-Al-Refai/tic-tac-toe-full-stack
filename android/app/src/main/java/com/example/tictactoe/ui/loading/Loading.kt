package com.example.tictactoe.ui.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun LoadingPage(viewModel: LoadingViewModel) {
    val state = viewModel.gameState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (state.value.isLoading) {
            CircularProgressIndicator()
            Text(text = "Connecting...")
        }
        if (state.value.isConnectionError) {
            Text(text = "Field to connect to server")
            Button(onClick = { viewModel.connect() }) {
                Text(text = "Try again")
            }
        }
    }

}