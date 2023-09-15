package app.services

import app.Clock
import app.gameDomain.*
import app.jpa.entities.Game
import app.jpa.entities.GameSetting
import app.jpa.entities.User
import app.jpa.repositories.GamesRepository
import app.jpa.repositories.UsersRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GamesServiceImpl(private val gamesRepository: GamesRepository,
                       private val usersRepository: UsersRepository,
                       private val gameLogic: GameLogic,
                       private val clock: Clock
) : GamesService {

    @Transactional
    override fun createGame(
        player1: User,
        boardX: Int,
        boardY: Int,
        fleetschema: HashMap<ShipType, Int>,
        shotsperround: Int,
        roundtime: Long,
        setuptime: Long
    ): GameCreationInfo {
        return try {
            var requiredSize = 0
            fleetschema.forEach {
                requiredSize += requiredSize + (it.key.size * it.value)
            }
            if (boardX * boardY < requiredSize) return GameCreationInfo.GameCreationFailed

            val setting = GameSetting(boardX,boardY,shotsperround, setuptime,roundtime, fleetschema )
            val game = gameLogic.createNewGame(player1.id!!, clock.now(), setting)
            gamesRepository.save(game)
            return GameCreationInfo.GameCreated(game)
        } catch (e : Exception){
            GameCreationInfo.GameCreationFailed
        }
    }

    @Transactional(readOnly = true)
    override fun getGameById(id: Int): GetGameResult {

        val game = gamesRepository.findGameById(id) ?: return GetGameResult.GameNotFound
        return GetGameResult.GameFound(game)
    }

    @Transactional
    override fun playerReady(id: Int, user: User) : PlayerReadyResult {
        val game = gamesRepository.findGameById(id)
        return when (val res = gameLogic.playerReady(game!!, user.id!!)){
            is PlayerReadyResult.GameStarted -> {
                user.stats!!.incrementGames()
                usersRepository.save(user)
                gamesRepository.save(game)
                return res
            }
            else -> res
        }
    }

    override fun placeShips(id: Int, player: User, placements: List<Placement>): FleetPlacementResult {
        val game = gamesRepository.findGameById(id)!!
        val results = mutableListOf<ShipCreationResult>()

        placements.forEach {
            val positions = buildShipPosition(it.head, it.orientation, it.type.size)
            results.add(gameLogic.placeShip(game, player.id!!, positions, it.type, clock.now()))
        }

        if (results.any { it !is ShipCreationResult.ShipPlaced })return FleetPlacementResult.CouldNotBePlaced

        gamesRepository.save(game)
        return FleetPlacementResult.PlacedCorrectly(game)
    }

    @Transactional
    override fun makePlay(id: Int, shots: List<Position>, userShooting: User): PlayInfo {
        val game = gamesRepository.findGameById(id)
        val result = gameLogic.validateRound(game!!,Round(userShooting.id!!,shots),clock.now())

        return if (result== RoundResult.TurnComplete) {
            gamesRepository.save(game)
            PlayInfo.PlaySuccessfull(game)
        } else if (result== RoundResult.YouWon){
            userShooting.stats!!.incrementWins()
            usersRepository.save(userShooting)
            gamesRepository.save(game)
            PlayInfo.GameWon(game)
        }
        else PlayInfo.PlayUnsuccessful
    }
    @Transactional
    override fun joinFreeGame(user : User): GetGameResult {
        val game = gamesRepository.findGamesByStateIs("WP").firstOrNull { it.player1 != user.id } ?: return GetGameResult.GameNotFound
        game.secondPlayerJoins(user.id!!, clock.now())
        gamesRepository.save(game)
        return GetGameResult.GameFound(game)
    }

    @Transactional
    override fun updateGame(game: Game) : GetGameResult {
        return if( gamesRepository.existsById(game.id!!)){
            val updatedGame = gamesRepository.save(game)
            GetGameResult.GameFound(updatedGame)
        }else GetGameResult.GameNotFound
    }

    @Transactional
    override fun gameReady(gameID: Int, user: User): PlayerReadyResult {
        val game = gamesRepository.findGameById(gameID)!!
        val result = gameLogic.playerReady(game, user.id!!)
        user.stats!!.incrementGames()
        usersRepository.save(user)
        gamesRepository.save(game)
        return result
    }

    override fun clear(): Boolean {
        gamesRepository.deleteAll()
        return true
    }
    @Transactional
    override fun surrender(gameID: Int, user: User): SurrenderResult {
        val game = gamesRepository.findGameById(gameID)!!
        val hasStarted = game.hasStarted()
        val result = gameLogic.surrender(game, user.id!!)

        if (result == SurrenderResult.SurrenderAccepted && hasStarted){
            val winner = if (user.id == game.player1) game.player2 else game.player1
            val winnerUser = usersRepository.findUserById(winner!!)!!
            winnerUser.stats!!.incrementWins()
            usersRepository.save(winnerUser)
            gamesRepository.save(game)
        }
        return result
    }

    private fun buildShipPosition(head: Position, vector: Orientation, size: Int): List<Position>{
        val shipPosition = mutableListOf<Position>()
        if (vector == Orientation.Down){
            for (i in 0 until size){
                shipPosition.add(Position(head.x , head.y +i))
            }
        }else{
            for (i in 0 until size){
                shipPosition.add(Position(head.x + i, head.y ))
            }
        }
        return shipPosition.toList()
    }

    sealed class PlayInfo{

        data class PlaySuccessfull(val game: Game) : PlayInfo()

        data class GameWon(val game: Game) : PlayInfo()

        object PlayUnsuccessful : PlayInfo()
    }

    sealed class GameCreationInfo{
        object GameCreationFailed: GameCreationInfo()
        data class GameCreated(val game: Game): GameCreationInfo()
    }

    sealed class FleetPlacementResult{
        data class PlacedCorrectly(val game : Game) :FleetPlacementResult()
        object CouldNotBePlaced:FleetPlacementResult()
    }

    sealed class GetGameResult{
        object GameNotFound: GetGameResult()
        data class GameFound(val game: Game): GetGameResult()
        data class GamesFound(val games: ArrayList<Game>): GetGameResult()
    }

}