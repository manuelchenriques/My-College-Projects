package http

import app.BattleShipsApplication
import app.gameDomain.Position
import app.http.model.SimpleGameOutputModel
import app.http.model.UnprocessedPlacement
import app.http.model.UnprocessedShipInputModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
import kotlin.test.assertTrue


@SpringBootTest(classes = [BattleShipsApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameTests {
    @LocalServerPort
    var port: Int = 0

    fun createUser( client : WebTestClient ,username : String,password  : String){
        client.post().uri("/user")
                .bodyValue(
                        mapOf(
                                "username" to username,
                                "passwordinfo" to password
                        )
                )
                .exchange()
                .expectStatus().isCreated
                .expectHeader().value("location") {
                    assertTrue(it.startsWith("/user/"))
                }
    }
    fun signInUser( client : WebTestClient ,username : String,password  : String) : UserTests.TokenResponse{
        return client.post().uri("/user/signIn")
                .bodyValue(
                        mapOf(
                                "username" to username,
                                "passwordinfo" to password
                        )
                )
                .exchange()
                .expectStatus().isOk
                .expectBody(UserTests.TokenResponse::class.java)
                .returnResult()
                .responseBody!!

    }

    @Test
    fun `can create game and get game by ID and joins game`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username1 = UUID.randomUUID().toString()
        val passwordinfo1 = "Changeit123"
        createUser(client, username1, passwordinfo1)

        val username2 = UUID.randomUUID().toString()
        val passwordinfo2 = "Changeit123"
        createUser(client,username2,passwordinfo2)

        val signeduser1 = signInUser(client, username1, passwordinfo1)
        val signeduser2 = signInUser(client, username2, passwordinfo2)

       var x =  client.post().uri("/game")
                .header("Authorization", "Bearer ${signeduser1.token}")
                .bodyValue(
                        mapOf(
                                "boardX" to 8,
                                "boardY" to 8,
                                "fleetschema" to mapOf("submarine" to 1 , "destroyer" to 1),
                                "shotsperround" to 3,
                                "setupTime" to 100000,
                                "roundtime" to 100000

                        )
                ).exchange()
                .expectStatus().isCreated
           .expectBody()
           .toString()

        val resXD = client.put().uri("/game/join")
                .header("Authorization", "Bearer ${signeduser2.token}")
                .exchange()
                .expectStatus().isOk
                .expectBody(SimpleGameOutputModel::class.java)
                .returnResult()
        val res = resXD.responseHeaders.location
        val list = arrayListOf<UnprocessedPlacement>()
        list.add(UnprocessedPlacement("Down",2, 2, "Submarine"))
        list.add(UnprocessedPlacement("Down",5, 5, "Destroyer"))
        val body = UnprocessedShipInputModel(list)

        client.put().uri("${res}/ship")
                .header("Authorization", "Bearer ${signeduser2.token}")
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk
        client.put().uri("${res}/ship")
                .header("Authorization", "Bearer ${signeduser1.token}")
                .bodyValue(body)
                .exchange()
                .expectStatus().isOk

        client.put().uri("${res}/ready")
            .header("Authorization", "Bearer ${signeduser2.token}")
            .exchange()
            .expectStatus().isOk
        client.put().uri("${res}/ready")
            .header("Authorization", "Bearer ${signeduser1.token}")
            .exchange()
            .expectStatus().isOk

        val list2 = arrayListOf<Position>()
        list2.add(Position(2,2))
        list2.add(Position(2,3))
        list2.add(Position(0,0))
        client.put().uri("${res}/play")
                .header("Authorization", "Bearer ${signeduser1.token}")
                .bodyValue(mapOf("plays" to list2 ))
                .exchange()
                .expectStatus().isOk

       var game =  client.get().uri("$res")
            .header("Authorization", "Bearer ${signeduser1.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
       // assertTrue { game. == "{\"{\\\"x\\\":2,\\\"y\\\":2}\":\"Sunk\",\"{\\\"x\\\":2,\\\"y\\\":3}\":\"Sunk\",\"{\\\"x\\\":0,\\\"y\\\":0}\":\"Miss\"}" }
    }

    @AfterEach
    fun cleanDatabase(){
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
        client.get().uri("clearGames").exchange()
            .expectStatus().isOk
        client.get().uri("clearUsers").exchange()
            .expectStatus().isOk
    }
}