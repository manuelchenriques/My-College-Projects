package app.gameDomain
enum class Orientation{Down, Right}
data class Placement (val orientation: Orientation, val head : Position , val type: ShipType )