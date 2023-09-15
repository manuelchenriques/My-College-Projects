package app.http

import app.http.infra.LinkRelation
import app.http.infra.siren
import app.http.model.*
import app.jpa.entities.User
import app.services.*
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping(Uris.Users.USER)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        return when (val res = userService.createUser(input.username, input.passwordinfo)) {
            is UserCreationInfo.UserCreated -> {
                val usrRes = userService.getUserById(res.userID)
                val usr = if (usrRes is GetUserResult.UserFound) usrRes.user else null
                return ResponseEntity.status(201).header(
                    "Location",
                    Uris.Users.byId(res.userID.toString()).toASCIIString()
                ).body(
                    siren(
                        getUserHome(usr!!)
                    ) {
                        clazz("User")
                        link(Uris.Users.byId(usr.id.toString()), LinkRelation("self"))
                        entity(usr.stats!!, LinkRelation(Uris.Users.STATS)) {
                            link(Uris.Users.statsById(usr.stats!!.id.toString()), LinkRelation("self"))
                        }
                        action("logIn", URI(Uris.Users.TOKEN), HttpMethod.POST, "application/json", false) {
                            textField("username")
                            textField("passwordInfo")
                        }
                    }
                )
            }

            is UserCreationInfo.UserAlreadyExists -> Problem.response(409, Problem.userAlreadyExists)
            is UserCreationInfo.UnsafePassword -> Problem.response(400, Problem.insecurePassword)
        }
    }

    @GetMapping(Uris.Users.ME)
    fun getByToken(user: User): ResponseEntity<*> {
        return ResponseEntity.status(200)
            .body(
                siren(
                    getUserHome(user)
                ) {
                    clazz("User")
                    link(Uris.Users.byId(user.id.toString()), LinkRelation("self"))
                    entity(user.stats!!, LinkRelation(Uris.Users.STATS)) {
                        link(Uris.Users.statsById(user.stats!!.id.toString()), LinkRelation("self"))
                    }
                }
            )
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateTokenInputModel,response:HttpServletResponse): ResponseEntity<*> {

        return when (val res = userService.createToken(input.username, input.passwordinfo)) {
            is TokenCreationInfo.TokenCreated -> {
                val cookie = Cookie("Authorization",res.token.id)
                cookie.maxAge = 60*60*24
                cookie.path = "/"
                response.addCookie(cookie)
                return ResponseEntity.status(200)
                    .body(UserTokenCreateOutputModel(res.token.id!!))
            }

            is TokenCreationInfo.AuthenticationFailed -> Problem.response(401, Problem.authenticationFailed)
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*> {
        return when (val res = userService.getUserById(UUID.fromString(id))) {
            is GetUserResult.UserFound -> {
                val usr = res.user
                ResponseEntity.status(200)
                    .body(
                        siren(
                            getUserHome(usr)
                        ) {
                            clazz("User")
                            link(Uris.Users.byId(usr.id.toString()), LinkRelation("self"))
                            entity(usr.stats!!, LinkRelation(Uris.Users.STATS)) {
                                link(Uris.Users.statsById(usr.stats!!.id.toString()), LinkRelation("self"))
                            }
                        }
                    )
            }

            is GetUserResult.UserNotFound -> Problem.response(404, Problem.userNotFound)
            is GetUserResult.AuthenticationFailed -> Problem.response(401, Problem.authenticationFailed)
        }
    }

    @GetMapping(Uris.Users.STATS)
    fun getUserStatsById(user: User): ResponseEntity<*> {
        return when (val res = userService.getUserStats(user)) {
            is GetUserStatsInfo.UserStatFound -> ResponseEntity.status(200).body(
                siren(
                    res.userStats
                ) {
                    clazz("UserStat")
                    link(Uris.Users.byId(user.id.toString()), LinkRelation("parent"))
                }
            )
            else -> Problem.response(404, Problem.orderByInvalid)
        }

    }

    @GetMapping(Uris.LEADERBOARD)
    fun getLeaderboard(
        @RequestParam orderBy: String,
        @RequestParam top: Int,
        @RequestParam idx: Int,
        @RequestParam  name: Optional<String>
    ): ResponseEntity<*> {
        return when (val res = userService.getLeaderBoard(UserServiceImpl.OrderBy.valueOf(orderBy), top, idx ,
            name.orElseGet { null})) {
            is GetUserStatsInfo.UserStatsFound -> {
                val leader = mutableListOf<UserStatOutputModel>()

                res.userStats.forEach { userStat ->
                    if (userStat != null) {
                        leader.add(
                            UserStatOutputModel(
                                userStat.user!!.stats?.id!!,
                                userStat.user!!.username!!,
                                userStat.gamesPlayed!!,
                                userStat.gamesWon!!
                            )
                        )
                    }
                }
                if (res.hasNext && !res.hasPrev) {
                    return ResponseEntity.status(200).body(
                        siren(LeaderboardOutputModel(leader)) {
                            clazz("UserStat")
                            link(Uris.leaderboard(UserServiceImpl.OrderBy.valueOf(orderBy),top,idx + 1), LinkRelation("next"))
                        }

                    )
                } else if (!res.hasNext && res.hasPrev) {
                    return ResponseEntity.status(200).body(
                        siren(LeaderboardOutputModel(leader)) {
                            clazz("UserStat")
                            link(Uris.leaderboard(UserServiceImpl.OrderBy.valueOf(orderBy),top,idx - 1), LinkRelation("previous"))
                        }

                    )
                } else if (res.hasNext && res.hasPrev){
                    return ResponseEntity.status(200).body(
                        siren(LeaderboardOutputModel(leader)) {
                            clazz("UserStat")
                            link(Uris.leaderboard(UserServiceImpl.OrderBy.valueOf(orderBy),top,idx + 1), LinkRelation("next"))
                            link(Uris.leaderboard(UserServiceImpl.OrderBy.valueOf(orderBy),top,idx - 1), LinkRelation("previous"))
                        }

                    )
                }else{
                    return ResponseEntity.status(200).body(
                        siren(LeaderboardOutputModel(leader)) {
                            clazz("UserStat")
                        }

                    )
                }

            }

            else -> Problem.response(404, Problem.userNotFound)
        }
    }

    @GetMapping("/clearUsers")
    fun clearDatabase(): ResponseEntity<*> {
        return when (userService.clear()) {
            true -> ResponseEntity.status(200).body(true)
            false -> ResponseEntity.status(400).body(false)

        }
    }


    @GetMapping(Uris.Users.USER)
    fun getUserHome(user: User): UserHomeOutputModel {
        return UserHomeOutputModel(
            id = user.id.toString(),
            username = user.username!!,
            stats = user.stats!!.id!!
        )
    }
}

