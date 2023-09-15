package app.http.model

import app.gameDomain.Position
import app.gameDomain.ShipType
import app.gameDomain.squares.Squares
import app.userDomain.GameImage
import app.userDomain.serializeFleet
import app.userDomain.serializeMyBoar
import app.userDomain.serializeOpponentBoard
import com.google.gson.JsonObject


class GameCreationInputModelUnprocessed(
    val boardX: Int,
    val boardY: Int,
    val fleetschema : HashMap<String,Int>,
    val shotsperround : Int,
    val setupTime : Long,
    val roundtime : Long
)
class GameCreationInputModel(
    val boardX: Int,
    val boardY: Int,
    val fleetschema : HashMap<ShipType,Int>,
    val shotsperround : Int,
    val setupTime : Long,
    val roundtime : Long
)

class SimpleGameOutputModel(
        val id : Int
)

class GameImageOutputModel(
    val id:Int,
    val gameState: String,
    val myFleet: String,
    val opponentFleet: String,
    val myBoard: String,
    val opponentBoard: String,
    val remainingTime: Long
)

fun imageToOuput(image:GameImage):GameImageOutputModel{
    return GameImageOutputModel(
        id = image.id,
        gameState = image.gameState,
        myFleet = image.myFleet.serializeFleet(),
        opponentFleet = image.opponentFleet.serializeFleet(),
        myBoard = image.myBoard.serializeMyBoar(),
        opponentBoard = image.opponentBoard.serializeOpponentBoard(),
        remainingTime = image.remainingTime
    )


}
