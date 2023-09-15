package app.gameDomain

import java.util.*

data class Round (
    val player: UUID,
    val positions: List<Position>
        )