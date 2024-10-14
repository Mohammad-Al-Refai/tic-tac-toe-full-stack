package com.example.tictactoe.models

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: String? = null,
    val adminId: String? = null,
    val createdAt: String? = null,
    var playerId1: String? = null,
    var playerId2: String? = null,
    var player1Name: String? = null,
    var player2Name: String? = null,
    var playerIdTurn: String? = null,
    var isDone: Boolean = false,
    var isPrivate: Boolean = false,
    var currentCellType: CellState = CellState.NONE,
    var name: String = "",
    var board: Array<Array<CellState>> = arrayOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false
        if (adminId != other.adminId) return false
        if (createdAt != other.createdAt) return false
        if (playerId1 != other.playerId1) return false
        if (playerId2 != other.playerId2) return false
        if (player1Name != other.player1Name) return false
        if (player2Name != other.player2Name) return false
        if (playerIdTurn != other.playerIdTurn) return false
        if (isDone != other.isDone) return false
        if (isPrivate != other.isPrivate) return false
        if (currentCellType != other.currentCellType) return false
        if (name != other.name) return false
        if (!board.contentDeepEquals(other.board)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + adminId.hashCode()
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (playerId1?.hashCode() ?: 0)
        result = 31 * result + (playerId2?.hashCode() ?: 0)
        result = 31 * result + (player1Name?.hashCode() ?: 0)
        result = 31 * result + (player2Name?.hashCode() ?: 0)
        result = 31 * result + (playerIdTurn?.hashCode() ?: 0)
        result = 31 * result + isDone.hashCode()
        result = 31 * result + isPrivate.hashCode()
        result = 31 * result + currentCellType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        return result
    }
}
