package app.jpa.entities

import app.gameDomain.ShipType
import app.gameDomain.getShipTypeOrNull
import app.utils.Serializer
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "game_settings", schema = "dbo")
open class GameSetting(gridX: Int, gridY: Int, shotsPerRound: Int, setupTime: Long, roundDuration: Long, fleetSchema: HashMap<ShipType, Int>) {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "gridx", nullable = false)
    open var gridx: Int? = gridX

    @Column(name = "gridy", nullable = false)
    open var gridy: Int? = gridY

    @Column(name = "shotsperround", nullable = false)
    open var shotsperround: Int? = shotsPerRound

    @Column(name = "setuptime", nullable = false)
    open var setuptime: Long? = setupTime

    @Column(name = "roundtime", nullable = false)
    open var roundtime: Long? = roundDuration

    @Type(type = "jsonb")
    @Column(name = "fleetschema" )
    open var fleetschema: String? = null

    @OneToOne(mappedBy = "setting")
    @JsonIgnore
    open var games: Game? = null

    init {
        this.fleetschema = Serializer().serializeFleetSchema(fleetSchema)
    }

    fun defaultSettings(): GameSetting {

        val defaultMap = HashMap<ShipType, Int>()
        defaultMap.put("Carrier".getShipTypeOrNull()!!, 5)
        defaultMap.put("Battleship".getShipTypeOrNull()!!, 4)
        defaultMap.put("Cruiser".getShipTypeOrNull()!!, 3)
        defaultMap.put("Submarine".getShipTypeOrNull()!!, 3)
        defaultMap.put("Destroyer".getShipTypeOrNull()!!, 2)

        return GameSetting(
            10,
            10,
            5,
            240,
            30,
            defaultMap
        )
    }
}