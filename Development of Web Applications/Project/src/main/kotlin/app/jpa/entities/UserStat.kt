package app.jpa.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "user_stats", schema = "dbo")
open class UserStat {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "games_played", nullable = false)
    open var gamesPlayed: Int? = 0

    @Column(name = "games_won", nullable = false)
    open var gamesWon: Int? = 0

    @OneToOne(mappedBy = "stats")
    @JsonIgnore
    open var user: User? = null

    fun incrementGames(): Int?{
        gamesPlayed = gamesPlayed!! + 1
        return gamesPlayed
    }

    fun incrementWins(): Int?{
        gamesWon = gamesWon!! + 1
        return gamesWon
    }
}