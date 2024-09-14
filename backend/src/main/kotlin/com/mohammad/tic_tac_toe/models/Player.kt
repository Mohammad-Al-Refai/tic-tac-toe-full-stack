package com.mohammad.tic_tac_toe.models

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "players")
data class Player(
    @NotNull
    @Id
    val id: UUID? = null,
    @Column
    val name: String,
    @Column("sessionId")
    val sessionId: String,
    @Column("isactive")
    var isActive: Boolean,
)
