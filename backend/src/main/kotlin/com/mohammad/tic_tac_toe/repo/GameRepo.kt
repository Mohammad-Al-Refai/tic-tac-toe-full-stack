package com.mohammad.tic_tac_toe.repo

import com.mohammad.tec.tac_toe.models.Game
import com.mohammad.tec.tac_toe.responses.AvailableGamesItem
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameRepo : CoroutineCrudRepository<Game, UUID> {
    suspend fun findGamesByPlayerId1(id: UUID): Collection<Game>
    suspend fun findGamesByPlayerId2(id: UUID): Collection<Game>

    @Query(
        "SELECT * FROM games\n" +
                "where (player_id1 = $1 or player_id2 = $1) and isdone = false"
    )
    suspend fun findUnfinishedGamesForPlayerId(id: UUID): Collection<Game>

    @Query(
        "select games.id,games.name from games\n" +
                "inner join players on games.admin_id = players.id and players.isactive = 'true'\n" +
                "where (games.player_id1 is null or games.player_id2 is null)\n" +
                "and games.isprivate=false and games.isdone=false"
    )
    suspend fun findAvailableGames(): Collection<AvailableGamesItem>
}