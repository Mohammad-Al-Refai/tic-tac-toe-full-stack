package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.models.GameState
import com.example.tictactoe.network.TicTacToeService
import com.example.tictactoe.ui.NavHost.NavGraph
import com.example.tictactoe.ui.NavHost.NavHostViewModel
import com.example.tictactoe.ui.Routes
import com.example.tictactoe.ui.landing.Landing
import com.example.tictactoe.ui.landing.LandingViewModel
import com.example.tictactoe.ui.loading.LoadingPage
import com.example.tictactoe.ui.loading.LoadingViewModel
import com.example.tictactoe.ui.play.Play
import com.example.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.parameter.parametersOf
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    val ticTacToeService: TicTacToeService = koinInject()
    val navHostViewModel: NavHostViewModel = koinViewModel{ parametersOf(ticTacToeService) }
    NavGraph(navController = navController,navHostViewModel)
}



