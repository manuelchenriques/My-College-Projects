package services

import app.gameDomain.getShipTypeOrNull
import app.services.GamesServiceImpl
import app.services.GetUserResult
import app.services.UserCreationInfo
import app.services.UserServiceImpl
import kotlin.test.assertTrue


//@SpringBootTest(classes = [BattleShipsApplication::class])

class GameTest {

    lateinit var gamesService: GamesServiceImpl

    lateinit var userService: UserServiceImpl


    fun `create game, join and get by id`(){

        val usr1Res = userService.createUser("yeetus","IsAPassword1")
        val user1ID = (usr1Res as UserCreationInfo.UserCreated).userID
        val user1 = (userService.getUserById(user1ID) as GetUserResult.UserFound).user
        val usr2Res = userService.createUser("deletus","IsAPassword2")
        val user2ID = (usr2Res as UserCreationInfo.UserCreated).userID
        userService.getUserById(user2ID) as GetUserResult.UserFound


        val schemaFleet = hashMapOf(
            "sub".getShipTypeOrNull()!! to 1,
            "car".getShipTypeOrNull()!! to 1,
            "bat".getShipTypeOrNull()!! to 1
            )

        user1.id = null
        val gameRes = gamesService.createGame(user1,10,10,schemaFleet,1,5000,5000)
        assertTrue(gameRes is GamesServiceImpl.GameCreationInfo.GameCreationFailed)
    }

}