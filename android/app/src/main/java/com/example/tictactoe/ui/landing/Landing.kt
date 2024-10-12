package com.example.tictactoe.ui.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.models.Game
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Landing(viewModel: LandingViewModel) {
    val state = viewModel.gameState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.background(MaterialTheme.colorScheme.error)
            )
        },
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(text = "Tic", color = MaterialTheme.colorScheme.primary)
                        Text(text = "Tac", color = MaterialTheme.colorScheme.secondary)
                        Text(text = "Toe", color = MaterialTheme.colorScheme.tertiary)
                    }
                    Text(fontSize = 13.sp,text=state.value.clientName)

                }
            })
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AvailableGamesList(
                state.value.availableGames,
                onJoinClick = { viewModel.joinGame(it) },
                state.value.isGetAvailableGamesLoading
            )
        }
    }
}

@Composable
fun AvailableGamesList(games: List<Game>, onJoinClick: (Game) -> Unit, isLoading: Boolean) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Row {
                Text("Updating")
                CircularProgressIndicator()
            }
        }
        if (games.isEmpty()) {
            Text("No Games Available")
        }
        LazyColumn {
            items(games.size) {
                val game = games[it]
                ListItem(headlineContent = {
                    Text(game.name)
                }, trailingContent = {
                    Button(onClick = {
                        onJoinClick(game)
                    }) {
                        Text("Join")
                    }
                })
            }
        }
    }

}