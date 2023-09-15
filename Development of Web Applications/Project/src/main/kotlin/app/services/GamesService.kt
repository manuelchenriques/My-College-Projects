package app.services

import app.gameDomain.*
import app.jpa.entities.Game
import app.jpa.entities.User

interface GamesService {
    fun createGame(player1: User,
                   boardX : Int,
                   boardY : Int,
                   fleetschema : HashMap<ShipType,Int>,
                   shotsperround : Int,
                   roundtime : Long,
                   setuptime: Long
    ) : GamesServiceImpl.GameCreationInfo

    fun getGameById(id: Int): GamesServiceImpl.GetGameResult

    //ver se todos os barcos tao posicionados
    fun playerReady(id : Int, user: User) : PlayerReadyResult

    fun placeShips(id: Int,
                   player: User,
                   placements: List<Placement>
    ): GamesServiceImpl.FleetPlacementResult

    fun makePlay(id: Int, shots : List<Position>,userShooting : User) : GamesServiceImpl.PlayInfo

    fun joinFreeGame(user : User):GamesServiceImpl.GetGameResult

    fun updateGame(game: Game) : GamesServiceImpl.GetGameResult

    fun gameReady(gameID: Int, user: User): PlayerReadyResult
    fun clear(): Boolean

    fun surrender(gameID: Int, user: User): SurrenderResult
}