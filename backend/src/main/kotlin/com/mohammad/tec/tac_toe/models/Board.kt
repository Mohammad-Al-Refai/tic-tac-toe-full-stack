package com.mohammad.tec.tac_toe.models

class Board {
    private var board: Array<Array<String>> = arrayOf()
    fun loadBoard(array: Array<Array<String>>): Board {
        board = array
        return this
    }

    fun createEmptyBoard(): Array<Array<String>> {
        return arrayOf(
            arrayOf(CellState.NONE.toString(), CellState.NONE.toString(), CellState.NONE.toString()),
            arrayOf(CellState.NONE.toString(), CellState.NONE.toString(), CellState.NONE.toString()),
            arrayOf(CellState.NONE.toString(), CellState.NONE.toString(), CellState.NONE.toString())
        )
    }

    fun update(row: Int, column: Int, value: CellState): Boolean {
        if (board[row][column] != CellState.NONE.toString()) {
            return false
        }
        board[row][column] = value.toString()
        return true
    }

    fun get(): Array<Array<String>> {
        return board
    }

    fun isWin(): CellState {
        for (i in board.indices) {
            if (board[i][0] != "NONE" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return CellState.valueOf(board[i][0])
            }
            if (board[0][i] != "NONE" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return CellState.valueOf(board[0][i])
            }
        }
        if (board[0][0] != "NONE" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return CellState.valueOf(board[0][0])
        }
        if (board[0][2] != "NONE" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return CellState.valueOf(board[0][2])
        }
        return CellState.NONE
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