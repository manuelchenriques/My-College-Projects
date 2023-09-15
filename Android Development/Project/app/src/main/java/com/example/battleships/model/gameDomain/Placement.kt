package com.example.battleships.model.gameDomain

enum class Orientation{Down, Right}
data class Placement (
    val orientation: Orientation,
    val x : Int,
    val y : Int,
    val shipType: String
)