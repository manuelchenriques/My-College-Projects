package app.gameDomain

import app.jpa.entities.Board
import app.jpa.entities.Game
import app.jpa.entities.GameSetting
import app.gameDomain.squares.Squares
import app.userDomain.GameImage
import app.utils.Deserializer
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

@Component
class GameLogic {

    fun createNewGame(user1: UUID, now: Instant, setting: GameSetting): Game {
        return Game(
            user1,
            now,
            setting
        )
    }

    fun playerJoins(game: Game, user: UUID, now: Instant): PlayerJoinResult {
        if (game.state != Game.State.WAITING_SECOND_PLAYER.value) return PlayerJoinResult.NotAwaitingPlayer
        if (game.player1!! == user) return PlayerJoinResult.IsAlreadyPlaying
        game.secondPlayerJoins(user, now)
        return PlayerJoinResult.PlayerJoined
    }

    fun playerReady(game: Game, player: UUID): PlayerReadyResult{
        if(!game.isPlayer(player)) return  PlayerReadyResult.NotAPlayer
        if (game.hasStarted()) return PlayerReadyResult.NotInSetupPhase(game)

        val userLogic = if (game.isPlayer1(player)) PLAYER_1_LOGIC else PLAYER_2_LOGIC
        val myFleet = game.getPlayerFleet(player)!!
        val fleetSchema = Deserializer().deserializeFleetSchema(game.setting!!.fleetschema!!)

        fleetSchema.forEach {schemaEntry ->
            if (myFleet.filter { it.type == schemaEntry.key }.size != schemaEntry.value) return PlayerReadyResult.MissingShips
        }

        when(game.state){
            userLogic.isReady.value -> return PlayerReadyResult.StillWaiting(game)
            userLogic.otherIsReady.value -> {
                startGame(game);
                return PlayerReadyResult.GameStarted(game)
            }
            Game.State.SETUP_PHASE.value -> {
                game.state = userLogic.isReady.value
                return PlayerReadyResult.AwaitingOtherPlayer(game)
            }
        }

        return PlayerReadyResult.GameStarted(game)
    }

    private fun startGame(game: Game) {
        game.apply {
            this.state = Game.State.PLAYER_1_TURN.value
            this.deadline =
                (Instant.ofEpochMilli(this.deadline!!) + Duration.ofMillis(this.setting!!.setuptime!!)).toEpochMilli()
        }
    }

    fun surrender(game: Game, player: UUID): SurrenderResult{
        if  (game.isGameOver()) return SurrenderResult.GameIsOver
        if (!game.isPlayer(player))return SurrenderResult.NotAPlayer
        val pLogic = if (game.isPlayer1(player)) PLAYER_1_LOGIC else PLAYER_2_LOGIC
        game.apply {
            this.state = pLogic.otherWon.value
        }
        return SurrenderResult.SurrenderAccepted
    }

    fun placeShip(
        game: Game,
        player: UUID,
        positions: List<Position>,
        shipType: ShipType,
        now: Instant
    ): ShipCreationResult {

        if (game.hasStarted()) return ShipCreationResult.NotInStepUpPhase
        if (!game.isPlayer(player)) return ShipCreationResult.NotAPlayer
        if (positions.size != shipType.size) return ShipCreationResult.InvalidSize
        if (!positions.all { it.x == positions.first().x || it.y == positions.first().y}) return ShipCreationResult.InvalidPosition

        if (now > Instant.ofEpochMilli(game.deadline!!)){
            game.apply { this.deadline = null; this.state = Game.State.FAILED_TO_START.value }
            return ShipCreationResult.TooLate
        }

        val fleetSchema = Deserializer().deserializeFleetSchema(game.setting!!.fleetschema!!)
        if (!fleetSchema.contains(shipType)) return ShipCreationResult.InvalidShipType

        val targetBoard = game.getPlayerBoard(player)!!
        val targetFleet = game.getPlayerFleet(player)!!

        if (
            targetFleet.filter { it.type == shipType }.size >= fleetSchema.get(shipType)!!
        ) return ShipCreationResult.AlreadyPlaced

        positions.forEach {
            if(targetBoard.getPosition(it) != null) return ShipCreationResult.InvalidPosition
        }

        val ship = Ship(shipType)
        targetFleet.add(ship)
        positions.forEach { targetBoard.setPosition(it, Squares(ship, it, Squares.Types.Ship)) }

        game.savePlayerBoard(player, targetBoard)
        game.savePlayerFleet(player, targetFleet)
        return ShipCreationResult.ShipPlaced
    }

    fun validateRound(game: Game, round: Round, now: Instant): RoundResult {

        if (!game.isPlayer(round.player)) return RoundResult.NotAPlayer
        if (round.positions.size > game.setting!!.shotsperround!! || round.positions.isEmpty()) return RoundResult.InvalidShotsNumber

        return when (game.state) {
            Game.State.WAITING_SECOND_PLAYER.value -> RoundResult.GameHasNotStarted
            Game.State.FAILED_TO_START.value -> RoundResult.GameAlreadyEnded
            Game.State.SETUP_PHASE.value -> RoundResult.GameHasNotStarted
            Game.State.PLAYER_1_READY.value -> RoundResult.GameHasNotStarted
            Game.State.PLAYER_2_READY.value -> RoundResult.GameHasNotStarted
            Game.State.PLAYER_1_WON.value -> RoundResult.GameAlreadyEnded
            Game.State.PLAYER_2_WON.value -> RoundResult.GameAlreadyEnded
            Game.State.PLAYER_1_TURN.value -> applyRound(game,round, PLAYER_1_LOGIC,now)
            Game.State.PLAYER_2_TURN.value -> applyRound(game,round, PLAYER_2_LOGIC,now)
            else -> {throw Exception()}
        }
    }

    private fun applyRound(game: Game, round: Round, playerLogic: PlayerLogic, now :Instant): RoundResult {

        if (!playerLogic.isTurn(game, round.player)) return RoundResult.NotYourTurn

        if (now > Instant.ofEpochMilli(game.deadline!!)){
            game.apply { this.deadline = null; this.state = playerLogic.otherWon.value }
            return RoundResult.TooLate
        }

        val targetBoard = game.getOpponentBoard(round.player)!!
        val targetFleet = game.getOpponentFleet(round.player)!!

        //TODO("Optimizar?")
        round.positions.forEach {
            if (targetBoard.getPosition(it) != null && targetBoard.getPosition(it)!!.type != Squares.Types.Ship)
                return RoundResult.InvalidPosition
        }

        round.positions.forEach {
            shoot(targetBoard, targetFleet, it)
        }

        game.saveOpponentFleet(round.player, targetFleet)
        game.saveOpponentBoard(round.player, targetBoard)

        if (game.isGameOver()){
            game.apply {
                this.state = playerLogic.iWon.value
                this.deadline = null
            }
            return RoundResult.YouWon
        }

        game.apply {
            this.state = playerLogic.nextPlayer.value
            this.deadline =
                (Instant.ofEpochMilli(this.deadline!!) + Duration.ofMillis(this.setting!!.roundtime!!)).toEpochMilli()
        }
        return RoundResult.TurnComplete
    }

    private fun shoot(board: Board, fleet: ArrayList<Ship>, position: Position): ShotResult {
        val target = board.getPosition(position)

        if (target == null){
            board.setPosition(position, Squares(null, position,Squares.Types.Miss))
            return ShotResult.MISS
        }

        if (target.type == Squares.Types.Ship){
            val ship = fleet.single { it.id == target.ship!!.id }

            board.setPosition(position, Squares(ship,position,Squares.Types.Hit))


            if (!ship.isSunk()) return ShotResult.HIT

            board.sunkShip(ship)
            ShotResult.SHIP_SUNK
        }

        return ShotResult.INVALID_POSITION
    }

    class PlayerLogic(
        val isTurn: (game: Game, user: UUID) -> Boolean,
        val isReady: Game.State,
        val otherIsReady: Game.State,
        val otherWon: Game.State,
        val iWon: Game.State,
        val nextPlayer: Game.State
    )
    companion object {
        private val PLAYER_1_LOGIC = PlayerLogic(
            isTurn = { game, user -> game.isPlayer1(user) },
            isReady = Game.State.PLAYER_1_READY,
            otherIsReady = Game.State.PLAYER_2_READY,
            otherWon = Game.State.PLAYER_2_WON,
            iWon = Game.State.PLAYER_1_WON,
            nextPlayer = Game.State.PLAYER_2_TURN
        )
        private val PLAYER_2_LOGIC = PlayerLogic(
            isTurn = { game, user -> game.isPlayer2(user) },
            isReady = Game.State.PLAYER_2_READY,
            otherIsReady = Game.State.PLAYER_1_READY,
            otherWon = Game.State.PLAYER_1_WON,
            iWon = Game.State.PLAYER_2_WON,
            nextPlayer = Game.State.PLAYER_1_TURN
        )
    }
}

sealed class ShipCreationResult {
    object NotAPlayer : ShipCreationResult()
    object NotInStepUpPhase : ShipCreationResult()
    object InvalidSize : ShipCreationResult()
    object InvalidShipType : ShipCreationResult()
    object InvalidPosition : ShipCreationResult()
    object AlreadyPlaced : ShipCreationResult()
    object ShipPlaced : ShipCreationResult()
    object TooLate : ShipCreationResult()
}

sealed class RoundResult {
    object InvalidShotsNumber: RoundResult()
    object InvalidPosition: RoundResult()
    object NotYourTurn : RoundResult()
    object GameHasNotStarted: RoundResult()
    object GameAlreadyEnded : RoundResult()
    object NotAPlayer : RoundResult()
    object TooLate : RoundResult()
    object YouWon : RoundResult()
    object TurnComplete : RoundResult()
}

sealed class PlayerJoinResult {
    object IsAlreadyPlaying: PlayerJoinResult()
    object PlayerJoined: PlayerJoinResult()
    object NotAwaitingPlayer: PlayerJoinResult()
}

sealed class PlayerReadyResult {
    data class StillWaiting(val game: Game): PlayerReadyResult()
    object NotAPlayer: PlayerReadyResult()
    data class AwaitingOtherPlayer(val game: Game): PlayerReadyResult()
    data class GameStarted(val game: Game): PlayerReadyResult()
    object MissingShips: PlayerReadyResult()
    data class NotInSetupPhase(val game: Game): PlayerReadyResult()
}

sealed class SurrenderResult {
    object NotAPlayer: SurrenderResult()
    object GameIsOver: SurrenderResult()
    object SurrenderAccepted: SurrenderResult()
}

enum class ShotResult{
    HIT,
    SHIP_SUNK,
    MISS,
    INVALID_POSITION
}

fun Game.getGameImage(user: UUID, now: Instant): GameImage? {
    if (!this.isPlayer(user)) return null
    val state = this.state.toString()
    val player1 = this.player1!!
    val player2 = this.player2
    val myBoard = this.getPlayerBoard(user)
    val myFleet = getFleetState(this.getPlayerFleet(user)!!)
    val otherBoard = this.getOpponentBoard(user)!!.getFilteredGrid()
    val otherFleet = getFleetState(this.getOpponentFleet(user)!!)
    val remainingTime : Long = if(this.deadline != null)
        now.until(Instant.ofEpochSecond(this.deadline!!), ChronoUnit.SECONDS)
    else 0
    val time = if (remainingTime < 0) 0 else remainingTime

    return GameImage(this.id!!,player1,player2,  state, myFleet, otherFleet, myBoard!!.getGrid(), otherBoard, time/*, this.setting!!*/)
}

private fun Game.isPlayer(user: UUID): Boolean = (isPlayer1(user) || isPlayer2(user))

private fun Game.isPlayer1(player: UUID): Boolean = this.player1 == player

private fun Game.isPlayer2(player: UUID): Boolean  = this.player2 == player

private fun Game.getPlayerBoard(player: UUID): Board?{
    if (!this.isPlayer(player)) return null
    if (this.isPlayer1(player)) return this.getGameBoard1()
    return this.getGameBoard2()
}

private fun Game.savePlayerBoard(player: UUID, board: Board){
    if (!this.isPlayer(player)) return
    if (this.isPlayer1(player)) return this.saveGameBoard1(board)
    this.saveGameBoard2(board)
}

private fun Game.getOpponentBoard(player: UUID): Board?{
    if (!this.isPlayer(player)) return null
    if (this.isPlayer1(player)) return this.getGameBoard2()
    return this.getGameBoard1()
}

private fun Game.saveOpponentBoard(player: UUID, board: Board) {
    if (!this.isPlayer(player)) return
    if (this.isPlayer1(player)) return this.saveGameBoard2(board)
    this.saveGameBoard1(board)
}

private fun Game.getPlayerFleet(player: UUID): ArrayList<Ship>?{
    if (!this.isPlayer(player)) return null
    if (this.isPlayer1(player)) return this.getGameFleet1()
    return this.getGameFleet2()
}

private fun Game.savePlayerFleet(player: UUID, fleet: ArrayList<Ship>) {
    if (!this.isPlayer(player)) return
    if (this.isPlayer1(player)) return this.saveGameFleet1(fleet)
    return this.saveGameFleet2(fleet)
}

private fun Game.getOpponentFleet(player: UUID): ArrayList<Ship>?{
    if (!this.isPlayer(player)) return null
    if (this.isPlayer1(player)) return this.getGameFleet2()
    return this.getGameFleet1()
}

private fun Game.saveOpponentFleet(player: UUID, fleet: ArrayList<Ship>) {
    if (!this.isPlayer(player)) return
    if (this.isPlayer1(player)) return this.saveGameFleet2(fleet)
    return this.saveGameFleet1(fleet)
}

private fun getFleetState(fleet: ArrayList<Ship>): MutableList<ShipCount> {
    val result = mutableListOf<ShipCount>()
    val types = fleet.distinctBy { it.type }.map { it.type }
    types.forEach { result.add(ShipCount(it,fleet.count { ship -> ship.type == it && !ship.isSunk() }) )}
    return result
}
