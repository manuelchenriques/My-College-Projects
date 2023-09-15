package com.example.battleships.model

import android.util.Log
import com.example.battleships.model.gameDomain.*
import com.example.battleships.model.gameDomain.squares.Squares
import com.example.battleships.utils.hypermedia.SirenModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


data class User(
    val username: String
){
    init {
        require(username.isNotBlank())
    }
}

data class UserToken(
    val token: String
)

data class GameImage(
    val id: Int,
    val player1: UUID,
    val player2: UUID?,
    val gameState: String,
    val myFleet: MutableList<ShipCount>,
    val opponentFleet: MutableList<ShipCount>,
    val myBoard: HashMap<Position, String>,
    val opponentBoard: HashMap<Position, String>,
    val remainingTime: Long
)

data class GameResponse(
    val id: Int,
    val player1: UUID,
    val player2: UUID?,
    val gameState: String,
    val myFleet: MutableList<ShipCount>,
    val opponentFleet: MutableList<ShipCount>,
    val myBoard: ArrayList<Squares>,
    val opponentBoard: HashMap<String, String>,
    val remainingTime: Long
)

fun GameResponse.convert():GameImage{
    val aux = hashMapOf<Position,String>()
    this.myBoard.forEach { squares ->
        aux[squares.pos] = squares.type.name
    }
    val auxOp = hashMapOf<Position,String>()
    this.opponentBoard.forEach { entry ->
        val auxPos = entry.key.removeSurrounding("Position(x=",")")
        val auxX = auxPos.takeWhile { it.isDigit() }.toInt()
        val auxY = auxPos.takeLastWhile { it.isDigit() }.toInt()
        auxOp[Position(auxX,auxY)] = entry.value
    }
    return GameImage(this.id,this.player1,this.player2,this.gameState,this.myFleet,this.opponentFleet,aux,auxOp,this.remainingTime)
}


class GameCreationInputModel(
    val boardX: Int,
    val boardY: Int,
    val fleetschema : HashMap<String,Int>,
    val shotsperround : Int,
    val setupTime : Long,
    val roundtime : Long
)

data class PlayInputModel(
    val plays : List<Position>
)

data class Stat(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val gamesWon: Int
)

data class LeaderBoard(
    val stats: List<Stat>
)


data class Developers(
    val name: String,
    val number:Int,
    val publicMail:String,
    val gitPage:String
)

data class PlacementsInputModel(
    val inputs: List<Placement>
)

fun Map<ShipType, Pair<Orientation, Position>>.toInputModel(): PlacementsInputModel {
    val input = mutableListOf<Placement>()
    this.forEach { entry ->
        input.add(Placement(entry.value.first,entry.value.second.x,entry.value.second.y,entry.key.name))
    }
    return PlacementsInputModel(input)
}


data class AboutInfo(
    val developers: ArrayList<Developers>,
    val from:String,
    val course:String,
    val professor:String
)

