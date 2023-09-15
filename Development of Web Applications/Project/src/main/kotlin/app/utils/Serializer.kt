package app.utils

import app.jpa.entities.Board
import app.gameDomain.Position
import app.gameDomain.Ship
import app.gameDomain.ShipCount
import app.gameDomain.ShipType
import app.gameDomain.squares.Squares
import com.google.gson.Gson

class Serializer {

    fun serializeBoard(board: Board): String{
        return Gson().toJson(board)
    }

    fun serializeGrid(gridToSerial:ArrayList<Squares>): String {

        val gson = Gson()
        val ret1 = gson.toJson(gridToSerial)

        return ret1
    }

    fun serializeFleet(fleet: ArrayList<Ship>): String{
        val gson = Gson()
        return gson.toJson(fleet)
    }

    fun serializeFleetSchema(fleetSchema:HashMap<ShipType, Int>): String {
        val gson = Gson()
        val newHash = hashMapOf<String,Int>()
        fleetSchema.forEach {
            newHash[gson.toJson(it.key)] = it.value
        }
        return gson.toJson(newHash)
    }

    fun serializeImageFleet(map: MutableList<ShipCount>): String {
        val gson = Gson()
        return gson.toJson(map)
    }

    fun serializeImageBoard(hash : HashMap<Position,String>):String{
        val gson = Gson()
        val newHash = hashMapOf<String,String>()
        hash.forEach {
            newHash[gson.toJson(it.key)] = it.value
        }
        return gson.toJson(newHash)

    }

}