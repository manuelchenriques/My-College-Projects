package app.utils;

import app.jpa.entities.Board
import app.gameDomain.Placement
import app.gameDomain.Position
import app.gameDomain.Ship
import app.gameDomain.ShipType
import app.gameDomain.squares.Squares;
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Deserializer {

    fun deserializeBoard(boardJSON: String): Board {
        return Gson().fromJson(boardJSON, Board::class.java)
    }

    fun deserializeGrid(gridString: String): ArrayList<Squares> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Squares>>() {}.type

        return gson.fromJson(gridString,type)
    }
    fun desirializeShipPlacements( placements: String): List<Placement>{
        val gson = Gson()
        val type = object : TypeToken< List<Placement>>() {}.type
        return gson.fromJson(placements,type)

    }
    fun desirializePlays( plays : String): List<Position>{
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Position>>() {}.type
        return gson.fromJson(plays,type)
    }


    fun deserializeFleet(fleet: String): ArrayList<Ship>{
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Ship>>() {}.type

        return gson.fromJson(fleet, type)
    }

    fun deserializeFleetSchema(fleetSchema: String): HashMap<ShipType, Int> {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<HashMap<String, Int>>() {}.type
        val newHash:HashMap<String, Int> = gson.fromJson(fleetSchema, type)
        val ret = HashMap<ShipType,Int>()

        newHash.forEach {
            ret[gson.fromJson(it.key,ShipType::class.java)] = it.value
        }
        return ret
    }
}