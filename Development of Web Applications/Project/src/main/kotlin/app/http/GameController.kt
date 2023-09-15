package app.http

import app.RealClock
import app.gameDomain.PlayerReadyResult
import app.gameDomain.SurrenderResult
import app.gameDomain.getGameImage
import app.http.infra.LinkRelation
import app.http.infra.siren
import app.http.model.*
import app.jpa.entities.Game
import app.jpa.entities.User
import app.services.GamesService
import app.services.GamesServiceImpl
import org.springframework.core.convert.ConversionService
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class GameController (private val gamesService: GamesService , val conversionService: ConversionService ){

    @PostMapping(Uris.Games.GAME_CREATE)
    fun create(user : User, @RequestBody unprocessed: GameCreationInputModelUnprocessed): ResponseEntity<*> {
        val input = conversionService.convert(unprocessed,GameCreationInputModel::class.java)
        val res = gamesService.createGame(
            user,
            input!!.boardX,
            input.boardY,
            input.fleetschema,
            input.shotsperround,
            input.roundtime,
            input.setupTime
        )

        return when (res){
            is GamesServiceImpl.GameCreationInfo.GameCreated -> ResponseEntity.status(201)
                .header( "Location",
                    Uris.Games.byId(res.game.id!!).toASCIIString())
                .body(
                siren(res.game.getGameImage(user.id!!,RealClock.now())!!){
                    clazz("Game")
                    action("place ship",Uris.Games.placeShip(res.game.id!!),HttpMethod.PUT,"shipPlacement",true){

                    }
                    link(Uris.Games.byId(res.game.id!!), LinkRelation("self") )
                }

            )


            is GamesServiceImpl.GameCreationInfo.GameCreationFailed -> Problem.response(404, Problem.gameNotFound)
        }
    }

    @PostMapping(Uris.Games.JOIN_OR_CREATE)
    fun joinGameOrCreate(user: User, @RequestBody unprocessed: GameCreationInputModelUnprocessed):ResponseEntity<*>{

        return when(val res = gamesService.joinFreeGame(user)){
            is GamesServiceImpl.GetGameResult.GameFound ->ResponseEntity.status(200).header(
                "Location",
                Uris.Games.byId(res.game.id!!).toASCIIString())
                .body(
                    siren(res.game.getGameImage(user.id!!,RealClock.now())!!){
                        clazz("Game")
                        action("place ship",Uris.Games.placeShip(res.game.id!!),HttpMethod.PUT,"shipPlacement",true){

                        }
                        link(Uris.Games.byId(res.game.id!!), LinkRelation("self") )
                    }

                )
           else -> return create(user, unprocessed)

        }
    }
    @PostMapping(Uris.Games.JOIN_GAME)
    fun joinGame(user: User):ResponseEntity<*>{

        return when(val res = gamesService.joinFreeGame(user)){
            is GamesServiceImpl.GetGameResult.GameFound ->ResponseEntity.status(200).header(
                "Location",
                Uris.Games.byId(res.game.id!!).toASCIIString())
                .body(
                    siren(res.game.getGameImage(user.id!!,RealClock.now())!!){
                        clazz("Game")
                        action("place ship",Uris.Games.placeShip(res.game.id!!),HttpMethod.PUT,"shipPlacement",true){

                        }
                        link(Uris.Games.byId(res.game.id!!), LinkRelation("self") )
                    }

                )
            is GamesServiceImpl.GetGameResult.GameNotFound -> Problem.response(404, Problem.gameNotFound)
            else -> ResponseEntity.status(500).build<Unit>()
        }
    }

    @PostMapping(Uris.Games.GAME_PLACE_SHIP)
    fun placeShip(usr: User, @PathVariable gameID : Int, @RequestBody shipPlacements : UnprocessedShipInputModel):ResponseEntity<*>{
       val placement = conversionService.convert(shipPlacements,ShipInputModel::class.java)
        return if (placement != null) {
            when(val res = gamesService.placeShips(gameID,usr,placement.inputs)){
                is GamesServiceImpl.FleetPlacementResult.PlacedCorrectly ->ResponseEntity.status(200).body(

                    siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                        clazz("Game")
                        action("ready",Uris.Games.gameReady(gameID),HttpMethod.PUT,"",true){

                        }
                        link(Uris.Games.byId(gameID), LinkRelation("self") )
                    }

                )
                is GamesServiceImpl.FleetPlacementResult.CouldNotBePlaced ->Problem.response(404, Problem.gameNotFound)
            }
        } else Problem.response(404, Problem.invalidShipType)
    }

    @PostMapping(Uris.Games.GAME_READY)
    fun gameReady(usr: User, @PathVariable gameID: Int):ResponseEntity<*>{

        return when(val res = gamesService.gameReady(gameID, usr)) {
            is PlayerReadyResult.GameStarted -> ResponseEntity.status(200).body(

                siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                    clazz("Game")
                    action("play",Uris.Games.makePlay(gameID),HttpMethod.PUT,"application/json",true){
                        playField("plays")
                    }
                    link(Uris.Games.byId(gameID), LinkRelation("self") )
                }
            )
            is PlayerReadyResult.StillWaiting -> ResponseEntity.status(200).body(
                siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                    clazz("Game")
                    action("ready",Uris.Games.gameReady(gameID),HttpMethod.PUT,"",true){
                    }
                    link(Uris.Games.byId(gameID), LinkRelation("self") )
                }

            )
            is PlayerReadyResult.AwaitingOtherPlayer -> ResponseEntity.status(200).body(
                siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                    clazz("Game")
                    action("ready",Uris.Games.gameReady(gameID),HttpMethod.PUT,"",true){
                    }
                    link(Uris.Games.byId(gameID), LinkRelation("self") )
                }
            )
            is PlayerReadyResult.NotInSetupPhase -> ResponseEntity.status(200).body(

                siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                    clazz("Game")
                    action("play",Uris.Games.makePlay(gameID),HttpMethod.PUT,"application/json",true){
                        playField("plays")
                    }
                    link(Uris.Games.byId(gameID), LinkRelation("self") )
                }

            )
            is PlayerReadyResult.MissingShips -> Problem.response(400,Problem.missingShips)
            is PlayerReadyResult.NotAPlayer ->Problem.response(405,Problem.actionNotPermitted)
        }
    }

    @PostMapping(Uris.Games.GAME_MAKE_PLAY)
    fun makePlay(usr: User, @RequestBody plays : PlayInputModel, @PathVariable gameID : Int ):ResponseEntity<*>{
        println(plays.plays)
       return when(val res = gamesService.makePlay(gameID,plays.plays,usr) ){

           is GamesServiceImpl.PlayInfo.PlaySuccessfull ->ResponseEntity.status(200).body(
               siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                   clazz("Game")
                   action("play",Uris.Games.makePlay(gameID),HttpMethod.PUT,"application/json",true){
                        playField("plays")
                   }
                   link(Uris.Games.byId(gameID), LinkRelation("self") )
               }

           )
           is GamesServiceImpl.PlayInfo.GameWon -> ResponseEntity.status(200).body(
               siren(res.game.getGameImage(usr.id!!,RealClock.now())!!){
                   clazz("Game")
                   action("play",Uris.Games.makePlay(gameID),HttpMethod.PUT,"application/json",true){
                       playField("plays")
                   }
                   link(Uris.Games.byId(gameID), LinkRelation("self") )
               }

           )
           is GamesServiceImpl.PlayInfo.PlayUnsuccessful -> Problem.response(400, Problem.invalidShotPosition)
       }
    }

    @PostMapping(Uris.Games.GAME_SURRENDER)
        fun gameSurrender(user: User, @PathVariable gameID: Int): ResponseEntity<*>{
        return when(gamesService.surrender(gameID, user)){
                is SurrenderResult.SurrenderAccepted -> ResponseEntity.status(200).body(gameID)
                is SurrenderResult.NotAPlayer, SurrenderResult.GameIsOver -> Problem.response(405, Problem.actionNotPermitted)
            }
        }


    @GetMapping(Uris.Games.BY_ID)
    fun getById(user: User, @PathVariable id: Int) :ResponseEntity<*>{
        return when(val res = gamesService.getGameById(id)){
            is GamesServiceImpl.GetGameResult.GameFound -> ResponseEntity.status(200)
                .body(
                    siren(res.game.getGameImage(user.id!!,RealClock.now())!!){
                        clazz("Game")
                        action("place ship",Uris.Games.placeShip(res.game.id!!),HttpMethod.PUT,"shipPlacement",true){

                        }
                        link(Uris.Games.byId(res.game.id!!), LinkRelation("self") )
                    }

                )
            is GamesServiceImpl.GetGameResult.GameNotFound -> Problem.response(404, Problem.gameNotFound)
            else -> Problem.response(405,Problem.actionNotPermitted)
        }
    }
    @PutMapping(Uris.Games.BY_ID)
    fun updateById(@PathVariable game: Game) :ResponseEntity<*>{
        return when(val res = gamesService.updateGame(game)){
            is GamesServiceImpl.GetGameResult.GameFound -> ResponseEntity.status(200).body(res.game)
            is GamesServiceImpl.GetGameResult.GameNotFound -> Problem.response(404, Problem.gameNotFound)
            else -> ResponseEntity.status(500).build<Unit>()
        }
    }

//TODO("APAGAR ISTO")
    @GetMapping("/clearGames")
    fun clearDatabase() : ResponseEntity<*>{
        return when( gamesService.clear()){
            true ->ResponseEntity.status(200).body(true)
            false ->ResponseEntity.status(400).body(false)

        }
    }

}