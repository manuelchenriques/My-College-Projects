package com.example.battleships.model.gameDomain

import java.util.*

data class Round (
    val player: UUID,
    val positions: List<Position>
        )