package com.example.tictactoe.ui.landing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers



@Composable
fun Landing(viewModel: LandingViewModel) {
    val state = viewModel.gameState.collectAsState()
    LaunchedEffect(Dispatchers.IO){
        viewModel.getAvailableGamesEvery5Seconds()
    }
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Landing page")
            Text(text = "Available games: ${state.value?.availableGames?.size}")

        }
    }
}