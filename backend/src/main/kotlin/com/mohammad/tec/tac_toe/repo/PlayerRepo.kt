package com.mohammad.tec.tac_toe.repo

import com.mohammad.tec.tac_toe.models.Player
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface PlayerRepo: CoroutineCrudRepository<Player, UUID> {
   suspend fun deleteBySessionId(id:String): Player?
}