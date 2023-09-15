package com.example.battleships.model.gameDomain

data class Position(val x: Int,val y: Int){
    companion object{
        val list = List(100){
            Position(it%10,it/10)
        }
    }
}
