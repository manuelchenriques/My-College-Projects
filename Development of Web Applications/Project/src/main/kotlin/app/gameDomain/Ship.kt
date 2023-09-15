package app.gameDomain

import java.util.*

class Ship(val type: ShipType){

    val id: UUID = UUID.randomUUID()

    private var hits = 0

    fun incrementHit(){
        if ( hits >=  type.size) return
        ++hits
    }

    fun isSunk(): Boolean = hits == type.size
}

data class ShipCount(val type:ShipType,val count:Int)