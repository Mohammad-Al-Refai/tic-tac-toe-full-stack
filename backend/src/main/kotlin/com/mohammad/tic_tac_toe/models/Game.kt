package com.mohammad.tic_tac_toe.models

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "games")
data class Game(
    @NotNull
    @Id
    val id: UUID? = null,
    @NotNull
    @Column("admin_id")
    val adminId: UUID,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("player_id1")
    var playerId1: UUID? = null,
    @Column("player_id2")
    var playerId2: UUID? = null,
    @Column("player1_name")
    var player1Name: String? = null,
    @Column("player2_name")
    var player2Name: String? = null,
    @Column("player_id_turn")
    var playerIdTurn: UUID? = null,
    @Column("isdone")
    var isDone: Boolean = false,
    @Column("isprivate")
    var isPrivate: Boolean = false,
    @Column("current_cell_type")
    var currentCellType: CellState = CellState.NONE,
    var name: String = "",
    @Column
    var board: Array<Array<String>> = arrayOf(),
)
