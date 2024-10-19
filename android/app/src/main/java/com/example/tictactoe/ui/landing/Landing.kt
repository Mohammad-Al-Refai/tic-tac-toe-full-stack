package com.example.tictactoe.ui.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tictactoe.models.Game
import com.example.tictactoe.ui.components.CustomToolBar
import com.example.tictactoe.ui.theme.Typography

@Composable
fun Landing(
    viewModel: LandingViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val state = viewModel.gameState.collectAsState()
    val navBackStackEntry = viewModel.navHostController.currentBackStackEntryAsState()
    val lifecycle = navBackStackEntry.value?.lifecycle

    DisposableEffect(lifecycle) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.startGetAvailableGames()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    viewModel.stopGetAvailableGames()
                }
            }

        lifecycle?.addObserver(observer)
        onDispose {
            lifecycle?.removeObserver(observer)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CustomToolBar(state.value.clientName)
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (state.value.isJoiningGame) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Joining game...")
                    CircularProgressIndicator()
                }
            } else {
                AvailableGamesList(
                    state.value.availableGames,
                    onJoinClick = { viewModel.joinGame(it) },
                    state.value.isGetAvailableGamesLoading,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AvailableGamesList(
    games: List<Game>,
    onJoinClick: (Game) -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text("Updating", style = Typography.bodyMedium)
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    modifier =
                        Modifier
                            .size(20.dp)
                            .padding(start = 5.dp),
                )
            }
        }
        if (games.isEmpty()) {
            Text("No Games Available", style = Typography.bodyMedium)
        }
        LazyColumn {
            items(games.size) {
                val game = games[it]
                ListItem(headlineContent = {
                    Text(game.name, style = Typography.bodyLarge)
                }, trailingContent = {
                    Button(onClick = {
                        onJoinClick(game)
                    }) {
                        Text("Join", style = Typography.bodyLarge)
                    }
                })
            }
        }
    }
}
