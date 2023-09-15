package app.gameDomain

data class ShipType(val name: String, val size: Int){

    companion object{
        val defaultTypes: List<ShipType> = listOf(
            ShipType("Carrier", 5),
            ShipType("Battleship", 4),
            ShipType("Cruiser", 3),
            ShipType("Submarine", 2),
            ShipType("Destroyer",2 )
        )
        operator fun invoke(type:String) = defaultTypes.singleOrNull {
            it.name.lowercase().contains(type.lowercase())
        }

    }

}
    fun String.getShipTypeOrNull() = ShipType.defaultTypes.singleOrNull {
        it.name.lowercase().contains(this.lowercase())
    }
