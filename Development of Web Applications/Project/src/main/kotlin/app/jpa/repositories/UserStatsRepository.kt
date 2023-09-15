package app.jpa.repositories

import app.jpa.entities.UserStat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserStatsRepository: JpaRepository<UserStat,UUID> {

    @Query("select s from UserStat s order by s.gamesPlayed DESC")
    fun orderByGamesPlayed():List<UserStat>
    @Query("select s from UserStat s order by s.gamesWon DESC")
    fun orderByGamesWon(): List<UserStat>


}