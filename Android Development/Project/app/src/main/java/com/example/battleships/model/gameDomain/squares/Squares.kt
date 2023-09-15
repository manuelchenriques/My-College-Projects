package com.example.battleships.model.gameDomain.squares

import com.example.battleships.model.gameDomain.Position
import com.example.battleships.model.gameDomain.Ship

data class Squares(val ship: Ship? = null, val pos: Position, val type: Types){

    enum class Types {
        Miss,
        Hit,
        Sunk,
        Ship
    }
}


