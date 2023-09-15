package app.gameDomain.squares

import app.gameDomain.Position
import app.gameDomain.Ship

data class Squares(val ship: Ship? = null, val pos:Position, val type:Types){

    enum class Types {
        Miss,
        Hit,
        Sunk,
        Ship
    }
    init {
        require((ship == null && type == Types.Miss) || (ship != null && type != Types.Miss))

        if (type == Types.Hit){
            ship!!.incrementHit()
        }
    }
}


