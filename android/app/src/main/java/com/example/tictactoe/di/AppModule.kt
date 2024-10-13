package com.example.tictactoe.di

import androidx.navigation.NavHostController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.landing.LandingViewModel
import com.example.tictactoe.ui.loading.LoadingViewModel
import com.example.tictactoe.ui.play.PlayViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single {
            HttpClient(CIO) {
                install(WebSockets)
            }
        }
        single { TicTacToeService(get()) }

        viewModel {
                (
                    navController: NavHostController, ticTacToeService: TicTacToeService, gameState: StateFlow<GameState>,
                ),
            ->
            LandingViewModel(navController, get(), gameState, get())
        }
        viewModel { (navController: NavHostController, gameState: StateFlow<GameState>) ->
            LoadingViewModel(navController, gameState)
        }
        viewModel { (navController: NavHostController, ticTacToeService: TicTacToeService, gameState: StateFlow<GameState>) ->
            PlayViewModel(navController, get(), gameState, get())
        }
    }
