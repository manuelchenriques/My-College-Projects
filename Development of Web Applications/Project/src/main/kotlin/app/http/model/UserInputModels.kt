package app.http.model

import app.gameDomain.Placement
import app.gameDomain.Position

data class UserCreateInputModel(
    val username: String,
    val passwordinfo: String
)

data class PlayInputModel(
        val plays : List<Position>
)

data class UnprocessedPlacement(
        val orientation : String,
        val x : Int,
        val y : Int,
        val shipType : String
)
data class UnprocessedShipInputModel(val inputs : List<UnprocessedPlacement>)

data class ShipInputModel(val inputs : List<Placement>)
data class UserCreateTokenInputModel(
    val username: String,
    val passwordinfo: String
)