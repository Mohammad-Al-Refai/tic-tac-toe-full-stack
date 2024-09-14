package com.mohammad.tic_tac_toe.models

class Board {
    private var board: Array<Array<String>> = arrayOf()
    fun loadBoard(array: Array<Array<String>>): com.mohammad.tic_tac_toe.models.Board {
        board = array
        return this
    }

    fun createEmptyBoard(): Array<Array<String>> {
        return arrayOf(
            arrayOf(com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString()),
            arrayOf(com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString()),
            arrayOf(com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString(), com.mohammad.tic_tac_toe.models.CellState.NONE.toString())
        )
    }

    fun update(row: Int, column: Int, value: com.mohammad.tic_tac_toe.models.CellState): Boolean {
        if (board[row][column] != com.mohammad.tic_tac_toe.models.CellState.NONE.toString()) {
            return false
        }
        board[row][column] = value.toString()
        return true
    }

    fun get(): Array<Array<String>> {
        return board
    }

    fun isWin(): com.mohammad.tic_tac_toe.models.CellState {
        for (i in board.indices) {
            if (board[i][0] != "NONE" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return com.mohammad.tic_tac_toe.models.CellState.valueOf(board[i][0])
            }
            if (board[0][i] != "NONE" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return com.mohammad.tic_tac_toe.models.CellState.valueOf(board[0][i])
            }
        }
        if (board[0][0] != "NONE" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return com.mohammad.tic_tac_toe.models.CellState.valueOf(board[0][0])
        }
        if (board[0][2] != "NONE" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return com.mohammad.tic_tac_toe.models.CellState.valueOf(board[0][2])
        }
        return com.mohammad.tic_tac_toe.models.CellState.NONE
    }

    fun isDraw(): Boolean {
        var draw = true
        for (row in board) {
            if (row.contains("NONE")) {
                draw = false
                break
            }
        }
        return draw
    }
}