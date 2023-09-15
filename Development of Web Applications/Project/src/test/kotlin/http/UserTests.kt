package http

import app.BattleShipsApplication
import app.http.model.LeaderboardOutputModel
import app.jpa.entities.UserStat
import app.jpa.repositories.UserStatsRepository
import app.http.model.UserHomeOutputModel
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
import kotlin.test.assertTrue

@SpringBootTest(classes = [BattleShipsApplication::class],webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class UserTests (){
    @Autowired
    lateinit var userStatsRepository: UserStatsRepository
    // One of the very few places where we use property injection
    @LocalServerPort
    var port: Int = 0


    fun `can create an user`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val passwordinfo = "Changeit123"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
        client.post().uri("/user")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to passwordinfo
                )
            )
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value("location") {
                assertTrue(it.startsWith("/user/"))
            }
    }

@Test
    fun `can create an user, obtain a token, and access user home and get its stats`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "Changeit123"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
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

        // when: creating a token
        // then: the response is a 200
        val result = client.post().uri("/user/signIn")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to password
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(TokenResponse::class.java)
            .returnResult()
            .responseBody!!

        // when: getting the user home with a valid token
        // then: the response is a 200 with the proper representation
        val res1 = client.get().uri("/me")
            .header("Authorization", "Bearer ${result.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserHomeOutputModel::class.java)
            .returnResult()
            .responseBody!!
        userStatsRepository.save(UserStat().apply { id = res1.stats; gamesWon= 2 ; gamesPlayed= 3 })

        val res2 =  client.get().uri("user/stats")
            .header("Authorization", "Bearer ${result.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserStat::class.java)
            .returnResult()
            .responseBody!!
        assertTrue(res2.gamesPlayed ==0)
        val res3 = client.get().uri("user/leaderboard")
            .header("orderBy","Wins")
            .exchange()
            .expectStatus().isOk
    //        .expectBody(LeaderboardOutputModel::class.java)
       //     .returnResult()
     //       .responseBody!!
    //    assertTrue(res3.stats[0].gamesWon ==5)
        // when: getting the user home with an invalid token
        // then: the response is a 4001 with the proper problem
        client.get().uri("/me")
            .header("Authorization", "Bearer ${result.token}-invalid")
            .exchange()
            .expectStatus().isUnauthorized
            .expectHeader().valueEquals("WWW-Authenticate", "bearer")
    }

    class TokenResponse(
        val token: String
    )

    @Test
    fun `user creation produces an error if user already exists`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "Changeit123"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
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

        // when: creating the same user again
        // then: the response is a 400 with the proper tyoe
        client.post().uri("/user")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to password
                )
            )
            .exchange()
            .expectStatus().is4xxClientError
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("type").isEqualTo(
                "https://github.com/isel-leic-daw/2022-daw-leic52d-2022-daw-leic52d-g01/tree/main/docsdocs/problems/user-already-exists"
            )
    }

    @Test
    fun `user creation produces an error if password is weak`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "-"

        // when: creating a user
        // then: the response is a 400 with the proper type
        client.post().uri("/user")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to password
                )
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("type").isEqualTo(
                "https://github.com/isel-leic-daw/2022-daw-leic52d-2022-daw-leic52d-g01/tree/main/docsdocs/problems/insecure-password"
            )
    }

    @Test
    fun `can create an user and get it's stats`() {

        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val passwordinfo = "Changeit123"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
        client.post().uri("/user")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to passwordinfo
                )
            )
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value("location") {
                assertTrue(it.startsWith("/user/"))
            }
        client.post().uri("/user/signIn")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "passwordinfo" to passwordinfo
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(TokenResponse::class.java)
            .returnResult()
            .responseBody!!

     //   userStatsRepository.save(UserStat().apply { id =result.token gamesPlayed= 2 ; gamesWon =2 })
    }
}