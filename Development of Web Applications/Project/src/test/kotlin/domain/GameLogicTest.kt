package domain

import app.RealClock
import app.jpa.entities.Game
import app.jpa.entities.GameSetting
import app.jpa.entities.User
import app.gameDomain.*
import app.gameDomain.squares.Squares
import app.utils.Deserializer
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameLogicTest {

    private val logic = GameLogic()


    fun `Test game logic`(){
        val player1 = User("player1", "123").id!!
        val player2 = User("player2", "123").id!!
        val notAPlayer = User("Exploiter",  "123").id!!

        val schema: HashMap<ShipType, Int> = HashMap()
        schema[ShipType("Cruiser")!!]  = 2 //size 3
        schema[ShipType("Submarine")!!]  = 2 //size 2

        val settings = GameSetting(8, 8, 3, 400, 120, schema)
        val clock = RealClock

        val game = logic.createNewGame(player1, clock.now(), settings)

        var joinResult: PlayerJoinResult
        var startResult: PlayerReadyResult
        var addShipResult: ShipCreationResult
        var playResult: RoundResult

        //Try to ready when still awaiting a new player
        startResult = logic.playerReady(game, player1)
        assertTrue(startResult is PlayerReadyResult.NotInSetupPhase, "Player was ready while still waiting for a player")

        //Third player tries to ready while waiting froa a new player
        startResult = logic.playerReady(game, notAPlayer)
        assertTrue(startResult is PlayerReadyResult.NotAPlayer, "Non player was ready while still waiting for a player")

        //Try to place ship when still awaiting a new player
        addShipResult = logic.placeShip(game, player1, listOf(Position(1,1), Position(1,2)), ShipType.invoke("Submarine")!!, clock.now())
        assertTrue(addShipResult is ShipCreationResult.NotInStepUpPhase, "Ship placed while waiting for a player")

        //Try to make play when still awaiting a new player
        playResult = logic.validateRound(game, Round(player1, listOf(Position(1,1))), clock.now())
        assertTrue(playResult is RoundResult.GameHasNotStarted, "Player1 made a play while still waiting for a player")

        //Player tries to join the same game twice
        joinResult = logic.playerJoins(game, player1, clock.now())
        assertTrue(joinResult is PlayerJoinResult.IsAlreadyPlaying, "Same player joined the game twice")

        //Valid Player joins game
        joinResult = logic.playerJoins(game, player2, clock.now())
        assertTrue(joinResult is PlayerJoinResult.PlayerJoined, "Valid player failed to join")

        //Third player tries to join game
        joinResult = logic.playerJoins(game, notAPlayer, clock.now())
        assertTrue(joinResult is PlayerJoinResult.NotAwaitingPlayer, "Third player somehow joined the game")

        //Try to ready when both boards are empty
        startResult = logic.playerReady(game, player1)
        assertTrue(startResult is PlayerReadyResult.MissingShips, "Player1 is ready with all ships missing")

        //Try to add ship but the coordinates are not adjacent to one another
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(3,3)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.InvalidPosition, "Ships with non-adjacent coordinates should not be allowed")

        //Try to add ship with the wrong size
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(1,2), Position(3,3)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.InvalidSize, "A ship with an invalid size was created")

        //Try to add a ship whose shipType is not present in the game settings schema
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(1,2)),
            ShipType("Destroyer")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.InvalidShipType, "A ship with an invalid ShipType was created")

        //Non-player trys to add a ship
        addShipResult = logic.placeShip(
            game,
            notAPlayer,
            listOf(Position(1,1), Position(1,2)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.NotAPlayer, "Ship was created by a user that is not a player")

        //Places submarine correctly
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(1,2)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.ShipPlaced, "Correct ship insert failed (1st insert)")

        //One of the ships positions overlaps another ship partially
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(2,1)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.InvalidPosition, "Ship that partially overlaps another was created")

        //One of the ships positions overlaps another ship totally
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(1,1), Position(1,2)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.InvalidPosition, "Ship that totally overlaps another was created")

        //Places submarine correctly
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(2,1), Position(2,2)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.ShipPlaced, "Correct ship insert failed (2nd insert)")

        //All the ships of that type have already been placed
        addShipResult = logic.placeShip(
            game,
            player1,
            listOf(Position(3,1), Position(3,2)),
            ShipType("Submarine")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.AlreadyPlaced, "Number of ships from a certain ShipType above the FleetSchema indicated value")

        //Placing the remaining ships from player1
        for (i in 5..6){
            logic.placeShip(
                game,
                player1,
                listOf(Position(i,1), Position(i,2), Position(i,3)),
                ShipType("Cruiser")!!,
                clock.now()
            )
        }

        //Player1 tries to ready when all ships are placed
        startResult = logic.playerReady(game, player1)
        assertTrue(startResult is PlayerReadyResult.AwaitingOtherPlayer, "Player1 was not able to ready when all his ships were places")

        //Player1 tries to ready again
        startResult = logic.playerReady(game, player1)
        assertTrue(startResult is PlayerReadyResult.StillWaiting, "Player1 should be more patient")

        //Try to make play while in SetUp Phase
        val rr = logic.validateRound(game, Round(player1, listOf(Position(1, 1))), RealClock.now())
        assertTrue(rr is RoundResult.GameHasNotStarted, "Shots fired while in SetUp Phase")

        //Placing the remaining ships from player2
        for (i in 1..2){
            logic.placeShip(
                game,
                player2,
                listOf(Position(i,1), Position(i,2)),
                ShipType("Submarine")!!,
                clock.now()
            )
        }

        for (i in 5..6){
            logic.placeShip(
                game,
                player2,
                listOf(Position(i,1), Position(i,2), Position(i,3)),
                ShipType("Cruiser")!!,
                clock.now()
            )
        }

        //Start game when all ships have been placed
        startResult = logic.playerReady(game, player2)
        assertTrue(startResult is PlayerReadyResult.GameStarted, "Game failed to start when all conditions should have been met")

        //Try to place ship while not in SetUp Phase
        addShipResult = logic.placeShip(
            game,
            player2,
            listOf(Position(4,4), Position(4,5), Position(4,6)),
            ShipType("Cruiser")!!,
            clock.now()
        )
        assertTrue(addShipResult is ShipCreationResult.NotInStepUpPhase, "Ship placed while not in SetUp Phase")

        //Try to ready while not in SetUp Phase
        startResult = logic.playerReady(game, player1)
        assertTrue(startResult is PlayerReadyResult.NotInSetupPhase, "Game should have already started")

        //Try to make play on the wrong turn
        playResult = logic.validateRound(game, Round(player2, listOf(Position(4,4))), RealClock.now())
        assertTrue(playResult is RoundResult.NotYourTurn, "Players should have to wait for their turn before making a play")

        //Try to make play with invalid number of shots
        playResult = logic.validateRound(game, Round(player1, listOf()), RealClock.now())
        assertTrue(playResult is RoundResult.InvalidShotsNumber, "In order to make a play, the player must shoot at least once")

        playResult = logic.validateRound(
            game,
            Round(
                player1,
                listOf(Position(1,1), Position(2,2), Position(3,3), Position(4,4))
            )
            , RealClock.now()
        )
        assertTrue(playResult is RoundResult.InvalidShotsNumber, "According to the game settings, each round can only have a maximum of 3 shots")

        //Try to make play while not being a player
        playResult = logic.validateRound(game, Round(notAPlayer, listOf(Position(4,4))), RealClock.now())
        assertTrue(playResult is RoundResult.NotAPlayer, "Only players should be allowed to make plays, and not third-party users")

        //try to hit shot
        playResult = logic.validateRound(game, Round(player1, listOf(Position(1,1))), RealClock.now())
        assertTrue(playResult is RoundResult.TurnComplete, "Player 1 made a valid play, but something went wrong")

        val hitSquare = Deserializer().deserializeBoard(game.board2!!).getPosition(Position(1,1))
        assertEquals(hitSquare!!.type, Squares.Types.Hit, "The shot was successful but the shot was not saved correctly")

        //Try to miss shot
        playResult = logic.validateRound(game, Round(player2, listOf(Position(7,7))), RealClock.now())
        assertTrue(playResult is RoundResult.TurnComplete, "Player 2 made a valid play, but something went wrong")

        val missSquare = Deserializer().deserializeBoard(game.board1!!).getPosition(Position(7,7))
        assertEquals(missSquare!!.type, Squares.Types.Miss, "The round was not successful but the shot was no saved correctly")

        //Try to hit invalid position
        playResult = logic.validateRound(game, Round(player1, listOf(Position(1,1))), RealClock.now())
        assertTrue(playResult is RoundResult.InvalidPosition, "Player 1 was able to shot the same position twice")

        //Try to sunk ship
        playResult = logic.validateRound(game, Round(player1, listOf(Position(1,2))), RealClock.now())
        assertTrue(playResult is RoundResult.TurnComplete, "Player 1 successfully tried to sunk a ship, but something went wrong")

        val sunkSquares = listOf(
            Deserializer().deserializeBoard(game.board2!!).getPosition(Position(1,1)),
            Deserializer().deserializeBoard(game.board2!!).getPosition(Position(1,2))
        )
        assertTrue(sunkSquares.all { it!!.type == Squares.Types.Sunk }, "Ship somehow did not sunk")

        //Try to win game
        for (i in 5..6){
            logic.validateRound(game, Round(player2, listOf(Position(i,1), Position(i, 2), Position(i, 3))), RealClock.now())

            logic.validateRound(game, Round(player1, listOf(Position(i,i))), RealClock.now())
        }

        logic.validateRound(game, Round(player2, listOf(Position(1,1), Position(1, 2))), RealClock.now())
        logic.validateRound(game, Round(player1, listOf(Position(3,3))), RealClock.now())
        playResult = logic.validateRound(game, Round(player2, listOf(Position(2,1), Position(2, 2))), RealClock.now())

        assertTrue(playResult is RoundResult.YouWon, "Game should have ended")

        //Try to make play when the game has already ended
        playResult = logic.validateRound(game, Round(player1, listOf(Position(4,4))), RealClock.now())
        assertTrue(playResult is RoundResult.GameAlreadyEnded, "Player made a play when the game was already over")
    }

    @Test
    fun `test timeout during SetUp Phase`(){
        val player1 = User("player1", "123").id!!
        val player2 = User("player2", "123").id!!

        val schema: HashMap<ShipType, Int> = HashMap()
        schema[ShipType("Submarine")!!]  = 1 //size 2

        val settings = GameSetting(8, 8, 3, Duration.ofSeconds(2).toMillis(), Duration.ofSeconds(120).toMillis(), schema)
        val clock = RealClock
        val game = logic.createNewGame(player1, clock.now(), settings)
        logic.playerJoins(game, player2, clock.now())
        Thread.sleep(Duration.ofSeconds(3).toMillis())

        val placeShipResult = logic.placeShip(game, player2, listOf(Position(1,1), Position(1,2)), ShipType.invoke("Submarine")!!, clock.now())
        assertTrue(placeShipResult is ShipCreationResult.TooLate, "Player took more than than permitted but was not timedout")
        assertEquals(game.state!!, Game.State.FAILED_TO_START.value, "Game failed to start but the game state does not inform it")
    }

    @Test
    fun `test timeout for not making play`(){
        val player1 = User("player1", "123").id!!
        val player2 = User("player2", "123").id!!

        val schema: HashMap<ShipType, Int> = HashMap()
        schema[ShipType("Submarine")!!]  = 1 //size 2

        val settings = GameSetting(8, 8, 3, 400, 3, schema)
        val clock = RealClock
        val game = logic.createNewGame(player1, clock.now(), settings)
        logic.playerJoins(game, player2, clock.now())

        logic.placeShip(game, player2, listOf(Position(1,1), Position(1,2)), ShipType.invoke("Submarine")!!, clock.now())
        logic.placeShip(game, player1, listOf(Position(1,1), Position(1,2)), ShipType.invoke("Submarine")!!, clock.now())
        logic.playerReady(game, player1)
        logic.playerReady(game, player2)
        Thread.sleep(Duration.ofSeconds(4).toMillis())

        val round = logic.validateRound(game, Round(player1, listOf(Position(1,1))), clock.now())
        assertTrue(round is RoundResult.TooLate, "Player took too long to make a play but was not timed out")
        assertEquals(game.state!!, Game.State.PLAYER_2_WON.value, "Player 2 should have won")
    }

    @Test
    fun `test surrender`(){
        val player1 = User("player1", "123").id!!
        val player2 = User("player2", "123").id!!
        val notPlayer = User("player3", "123").id!!


        val schema: HashMap<ShipType, Int> = HashMap()
        schema[ShipType("Submarine")!!]  = 1 //size 2

        val settings = GameSetting(8, 8, 3, 400, 3, schema)
        val clock = RealClock
        val game = logic.createNewGame(player1, clock.now(), settings)
        logic.playerJoins(game, player2, clock.now())

        val failedSurrender = logic.surrender(game, notPlayer)
        assertTrue(failedSurrender is SurrenderResult.NotAPlayer, "A third-party was able to FF the game")

        val acceptedSurrender = logic.surrender(game, player1)
        assertTrue(acceptedSurrender is SurrenderResult.SurrenderAccepted, "Valid surrender was not accepted")
        assertEquals(game.state!!, Game.State.PLAYER_2_WON.value, "Surrender accepted but state was not updated")
    }


}