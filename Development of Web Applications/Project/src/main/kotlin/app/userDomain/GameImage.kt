package app.userDomain

import app.gameDomain.Position
import app.gameDomain.ShipCount
import app.gameDomain.squares.Squares
import app.jpa.entities.GameSetting
import app.utils.Serializer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class GameImage(
        val id: Int,
        val player1: UUID,
        val player2: UUID?,
        val gameState: String,
        val myFleet: MutableList<ShipCount>,
        val opponentFleet: MutableList<ShipCount>,
        val myBoard: ArrayList<Squares>,
        val opponentBoard: HashMap<Position, String>,
        val remainingTime: Long,
        //val settings: GameSetting
)

fun ArrayList<Squares>.serializeMyBoar() = Serializer().serializeGrid(this)

fun MutableList<ShipCount>.serializeFleet() = Serializer().serializeImageFleet(this)

fun HashMap<Position, String>.serializeOpponentBoard() = Serializer().serializeImageBoard(this)

