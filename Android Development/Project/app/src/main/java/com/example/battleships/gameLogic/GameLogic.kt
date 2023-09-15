package com.example.battleships.gameLogic

import com.example.battleships.model.gameDomain.Orientation
import com.example.battleships.model.gameDomain.Position
import com.example.battleships.model.gameDomain.ShipType

fun isPosIn(pos: Position, map: Map<ShipType, List<Position>>): ShipType?{
    var ret: ShipType? = null
    map.forEach { entry ->
        if (ret == null && entry.value.contains(pos)) ret = entry.key
    }
    return ret
}

fun Map<ShipType, List<Position>>.positionListToPlacement(): Map<ShipType, Pair<Orientation, Position>> {
    return this.mapValues { auxEntry ->
        if (auxEntry.value[0].y == auxEntry.value[1].y)
            Pair(Orientation.Right,auxEntry.value[0])
        else
            Pair(Orientation.Down,auxEntry.value[0])
    }
}

fun canBePut(pos: Position, ship: ShipType, orientation: Orientation, map: Map<ShipType, List<Position>>):Pair<Boolean,List<Position>>{
    var aux:Int = if (orientation == Orientation.Down){
        if (pos.y+ship.size > 10) return Pair(false, emptyList())
        pos.y
    }else{
        if (pos.x+ship.size > 10) return Pair(false, emptyList())
        pos.x
    }
    val posToPut = List(ship.size){
        if (orientation == Orientation.Down){
            Position(pos.x,aux++)
        }else {
            Position(aux++,pos.y)
        }
    }
    var canPut = false
    map.values.forEach { list ->
        canPut = list.any{
            posToPut.contains(it)
        }
    }
    return Pair(!canPut,posToPut)
}
