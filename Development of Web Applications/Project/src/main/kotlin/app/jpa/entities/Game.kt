package app.jpa.entities

import app.gameDomain.Ship
import app.utils.Deserializer
import app.utils.Serializer
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Entity

@Table(name = "games", schema = "dbo")
open class Game(user1ID: UUID, now: Instant, setting: GameSetting) {

    @Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "state", nullable = false, length = 64)
    open var state: String? = null

    @Column(name = "updated", nullable = false)
    open var updated: Long? = null

    @Column(name = "deadline")
    open var deadline: Long? = null

    @Column(name = "created", nullable = false)
    open var created: Long? = null

    @JoinColumn(name = "player1")
    open var player1: UUID? = user1ID

    @JoinColumn(name = "player2")
    open var player2: UUID? = null

    @Type(type = "jsonb")
    @Column(name = "board2", nullable = false)
    open var board2: String? = null

    @Type(type = "jsonb")
    @Column(name = "board1", nullable = false)
    open var board1: String? = null

    @Type(type = "jsonb")
    @Column(name = "fleet1")
    open var fleet1: String? = null

    @Type(type = "jsonb")
    @Column(name = "fleet2")
    open var fleet2: String? = null

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "setting", referencedColumnName = "id")
    open var setting: GameSetting? = setting

    init {
        this.state = State.WAITING_SECOND_PLAYER.value
        this.created = now.toEpochMilli()
        this.updated = now.toEpochMilli()
        this.board1 = Serializer().serializeBoard(Board(setting.gridx!!, setting.gridy!!))
        this.board2 = Serializer().serializeBoard(Board(setting.gridx!!, setting.gridy!!))
        this.fleet1 = Serializer().serializeFleet(arrayListOf())
        this.fleet2 = Serializer().serializeFleet(arrayListOf())
    }

    fun secondPlayerJoins(userID: UUID, now: Instant){
        this.player2 = userID
        this.state = State.SETUP_PHASE.value
        this.deadline = (now + Duration.ofMillis(this.setting!!.setuptime!!)).toEpochMilli()
    }

    fun getGameBoard1(): Board =
        Deserializer().deserializeBoard(this.board1!!)

    fun getGameBoard2(): Board =
        Deserializer().deserializeBoard(this.board2!!)

    fun getGameFleet1(): ArrayList<Ship> =
        Deserializer().deserializeFleet(this.fleet1!!)

    fun getGameFleet2(): ArrayList<Ship> =
        Deserializer().deserializeFleet(this.fleet2!!)

    fun saveGameBoard1(board: Board) {
        this.board1 = Serializer().serializeBoard(board)
    }

    fun saveGameBoard2(board: Board) {
        this.board2 = Serializer().serializeBoard(board)
    }

    fun saveGameFleet1(fleet: ArrayList<Ship>) {
        this.fleet1 = Serializer().serializeFleet(fleet)
    }

    fun saveGameFleet2(fleet: ArrayList<Ship>) {
        this.fleet2 = Serializer().serializeFleet(fleet)
    }

    fun hasStarted(): Boolean{
        if (
            this.state == State.WAITING_SECOND_PLAYER.value ||
            this.state == State.SETUP_PHASE.value ||
            this.state == State.PLAYER_1_READY.value ||
            this.state == State.PLAYER_2_READY.value
        ) return false
        return true
    }

    fun isGameOver(): Boolean{
        val f1 = Deserializer().deserializeFleet(fleet1!!)
        val f2 = Deserializer().deserializeFleet(fleet2!!)

        if (f1.any { !it.isSunk() } && f2.any { !it.isSunk() }) return false
        return true
    }

    fun getFleet1asArrayList(): ArrayList<Ship> = Deserializer().deserializeFleet(fleet1!!)

    fun getFleet2asArrayList(): ArrayList<Ship> = Deserializer().deserializeFleet(fleet2!!)

    enum class State(val value: String){
        WAITING_SECOND_PLAYER("WP"),
        SETUP_PHASE("SP"),
        PLAYER_1_READY("R1"),
        PLAYER_2_READY("R2"),
        PLAYER_1_TURN("PT1"),
        PLAYER_2_TURN("PT2"),
        PLAYER_1_WON("P1W"),
        PLAYER_2_WON("P2W"),
        FAILED_TO_START("F")
        ;
    }
}