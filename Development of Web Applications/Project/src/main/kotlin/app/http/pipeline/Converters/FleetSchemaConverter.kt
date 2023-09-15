package app.http.pipeline.Converters

import app.gameDomain.Position
import app.gameDomain.ShipType
import app.gameDomain.getShipTypeOrNull
import app.http.model.GameCreationInputModel
import app.http.model.GameCreationInputModelUnprocessed
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.core.convert.converter.Converter
import org.springframework.format.Formatter

class FleetSchemaConverter: Converter<GameCreationInputModelUnprocessed, GameCreationInputModel> {
    override fun convert(source:GameCreationInputModelUnprocessed): GameCreationInputModel {
        val ret :  HashMap<ShipType, Int> = hashMapOf()
        source.fleetschema.forEach{
            entry ->
            entry.key.getShipTypeOrNull()?.let { ret[it] = entry.value }

        }
        return GameCreationInputModel(source.boardX,source.boardY,ret,source.shotsperround,source.setupTime,source.roundtime)
    }
}