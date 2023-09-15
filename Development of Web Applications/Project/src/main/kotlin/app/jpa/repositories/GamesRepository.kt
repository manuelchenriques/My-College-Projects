package app.jpa.repositories

import app.jpa.entities.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GamesRepository : JpaRepository<Game,UUID> {
    @Query("select g from Game g where g.id = ?1")
    fun findGameById(id: Int) : Game?
    fun existsById(id: Int) : Boolean
    fun findGamesByStateIs( state : String) :ArrayList<Game>
}