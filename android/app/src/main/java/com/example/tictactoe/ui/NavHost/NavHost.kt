package com.example.tictactoe.ui.NavHost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import com.example.tictactoe.ui.landing.Landing
import com.example.tictactoe.ui.landing.LandingViewModel
import com.example.tictactoe.ui.loading.LoadingPage
import com.example.tictactoe.ui.loading.LoadingViewModel
import com.example.tictactoe.ui.play.Play
import com.example.tictactoe.ui.play.PlayViewModel
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavGraph(
    navController: NavHostController,
    gameState: StateFlow<GameState>,
    ticTacToeService: TicTacToeService
) {
    NavHost(navController = navController, startDestination = Routes.Loading.name) {
        composable(Routes.Loading.name) {
            val loadingViewModel: LoadingViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        gameState
                    )
                }
            LoadingPage(loadingViewModel)
        }
        composable(Routes.Landing.name) {
            val landingViewModel: LandingViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        ticTacToeService,
                        gameState
                    )
                }
            Landing(landingViewModel)
        }
        composable(Routes.Play.name) {
            val playViewModel: PlayViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        ticTacToeService,
                        gameState
                    )
                }
            Play(playViewModel)
        }
    }
}