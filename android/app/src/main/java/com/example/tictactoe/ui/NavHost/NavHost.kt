package com.example.tictactoe.ui.NavHost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.Routes
import com.example.tictactoe.ui.landing.Landing
import com.example.tictactoe.ui.landing.LandingViewModel
import com.example.tictactoe.ui.loading.LoadingPage
import com.example.tictactoe.ui.loading.LoadingViewModel
import com.example.tictactoe.ui.play.Play
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavGraph(navController: NavHostController, navHostViewModel: NavHostViewModel) {
    var gameState = navHostViewModel.ticTacToeService.gameStateFlow
    NavHost(navController = navController, startDestination = Routes.Loading.name) {
        composable(Routes.Loading.name) {
            val loadingViewModel: LoadingViewModel =
                koinViewModel {
                    parametersOf(
                        navController,
                        navHostViewModel.ticTacToeService,
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
                        navHostViewModel.ticTacToeService,
                        gameState
                    )
                }
            Landing(landingViewModel)
        }
        composable(Routes.Play.name) {
            Play()
        }
    }
}