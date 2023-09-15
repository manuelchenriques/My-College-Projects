package app.http

import app.services.UserServiceImpl
import org.springframework.web.util.UriTemplate
import java.net.URI

object Uris {

    const val HOME = "/"
    const val ABOUT = "/about"
    const val LEADERBOARD = "/leaderboard"
    fun home(): URI = URI(HOME)
    fun about():URI = URI(ABOUT)
    fun leaderboard(orderBy:UserServiceImpl.OrderBy,top: Int, idx: Int) =UriTemplate("$LEADERBOARD?orderBy={orderBy}&top={top}&idx={idx}").expand(orderBy ,top , idx)
   object Users {
       const val HOME = "/"
       const val ME = "/me"
       const val USER = "/user"
       const val BY_ID = "/user/{id}"
       const val TOKEN = "/user/signIn"
       const val STATS = "/user/stats"

       fun statsById(id: String) = UriTemplate(BY_ID).expand(id)
       fun byId(id: String) = UriTemplate(BY_ID).expand(id)
   }

    object Games{
        const val JOIN_OR_CREATE = "/game/fast"
        const val GAME_CREATE = "/game"
        const val JOIN_GAME = "/game/join"
        const val BY_ID = "/game/{id}"
        const val GAME_SURRENDER = "/game/{gameID}/surrender"
        fun byId(id: Int) = UriTemplate(BY_ID).expand(id)
        const val GAME_READY = "/game/{gameID}/ready"
        fun gameReady(id: Int) = UriTemplate(GAME_READY).expand(id)
        const val GAME_PLACE_SHIP = "/game/{gameID}/ship"
        fun placeShip(id: Int) = UriTemplate(GAME_PLACE_SHIP).expand(id)
        const val GAME_MAKE_PLAY = "/game/{gameID}/play"
        fun makePlay(id: Int) = UriTemplate(GAME_MAKE_PLAY).expand(id)
    }

}