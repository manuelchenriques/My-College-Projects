package app.http.pipeline.Converters

import app.gameDomain.Orientation
import app.gameDomain.Placement
import app.gameDomain.Position
import app.gameDomain.getShipTypeOrNull
import app.http.model.ShipInputModel
import app.http.model.UnprocessedShipInputModel
import org.springframework.core.convert.converter.Converter

class ShipInputModelConverter: Converter<UnprocessedShipInputModel, ShipInputModel> {
    override fun convert(source: UnprocessedShipInputModel): ShipInputModel? {
      val x =   source.inputs.map { x -> Placement( Orientation.valueOf(x.orientation) , Position(x.x,x.y),x.shipType.getShipTypeOrNull()!!) }
      return ShipInputModel(x)
    }
}