package com.example.tictactoe.ui.play

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R
import com.example.tictactoe.models.CellState

@Composable
fun Board(
    board: Array<Array<CellState>>,
    Iam: CellState,
    onCellClick: (row: Int, col: Int) -> Unit
) {

    Column {
        repeat(board.size) { row ->
            Row {
                repeat(board[row].size) { col ->
                    Cell(col = col, row = row, Iam = Iam, board = board, onCellClick = onCellClick)
                }
            }
        }
    }
}

@Composable
fun XOImage(cellState: CellState) {
    if (cellState == CellState.X) {
        Image(
            painter = painterResource(R.drawable.x),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
    }
    if (cellState == CellState.O) {
        Image(
            painter = painterResource(R.drawable.o),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
    }

}
@Composable
fun Cell(Iam:CellState,board:Array<Array<CellState>>,row:Int,col: Int,onCellClick:(row:Int,col:Int)->Unit){
    @Composable
    fun getCellColor(cell: CellState): Color {
        if (Iam == CellState.X && cell == CellState.X) {
            return MaterialTheme.colorScheme.secondary
        }
        if (Iam == CellState.X && cell == CellState.O) {
            return MaterialTheme.colorScheme.tertiary
        }
        if (Iam == CellState.O && cell == CellState.O) {
            return MaterialTheme.colorScheme.secondary
        }
        if (Iam == CellState.O && cell == CellState.X) {
            return MaterialTheme.colorScheme.tertiary
        }
        return MaterialTheme.colorScheme.primary
    }
    Button(
        modifier = Modifier
            .size(100.dp)
            .padding(10.dp),
        colors = ButtonColors(
            getCellColor(board[row][col]),
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primary
        ),
        onClick = { onCellClick(row, col) }) {
        XOImage(board[row][col])
    }
}