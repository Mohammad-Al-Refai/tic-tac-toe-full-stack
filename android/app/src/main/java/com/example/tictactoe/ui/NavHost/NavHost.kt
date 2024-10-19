package com.example.tictactoe.ui.NavHost

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoe.models.AppState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import com.example.tictactoe.ui.landing.Landing
import com.example.tictactoe.ui.landing.LandingViewModel
import com.example.tictactoe.ui.play.Play
import com.example.tictactoe.ui.play.PlayViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(
    navController: NavHostController,
    appState: StateFlow<AppState>,
    ticTacToeService: TicTacToeService,
    snackbarEvent: MutableSharedFlow<String>,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(navController = navController, startDestination = Routes.Landing.name) {
        composable(Routes.Landing.name) {
            val landingViewModel: LandingViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        ticTacToeService,
                        appState,
                    )
                }
            Landing(landingViewModel, snackbarHostState)
        }
        composable(Routes.Play.name) {
            val playViewModel: PlayViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        ticTacToeService,
                        appState,
                        snackbarEvent,
                        snackbarHostState,
                    )
                }
            Play(playViewModel, snackbarHostState)
            BackHandler(true) {
                // do nothing
            }
        }
    }
}
