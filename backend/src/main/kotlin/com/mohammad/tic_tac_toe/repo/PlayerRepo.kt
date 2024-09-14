package com.mohammad.tic_tac_toe.repo

import com.mohammad.tic_tac_toe.models.Player
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface PlayerRepo: CoroutineCrudRepository<Player, UUID> {
   suspend fun deleteBySessionId(id:String): Player?
}