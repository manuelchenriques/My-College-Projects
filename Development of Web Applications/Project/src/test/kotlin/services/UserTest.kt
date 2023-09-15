package services

import app.jpa.entities.User
import app.jpa.repositories.TokensRepository
import app.jpa.repositories.UserStatsRepository
import app.jpa.repositories.UsersRepository
import app.services.GetUserResult
import app.services.TokenCreationInfo
import app.services.UserCreationInfo
import app.services.UserServiceImpl
import app.userDomain.UserLogic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue


//@SpringBootTest(classes = [BattleShipsApplication::class])
@ExtendWith(MockitoExtension::class)
class UserTest {
    @Mock
    lateinit var usersRepository: UsersRepository
    @Mock
    lateinit var tokensRepository: TokensRepository
    @Mock
    lateinit var userStatsRepository: UserStatsRepository

    var userLogic = UserLogic()
    @InjectMocks
    lateinit var userService: UserServiceImpl
    lateinit var user : User
    @BeforeEach
    fun setup(){
        user =  User("userTest","UserTestPassword")
    }

    @Test
    fun `Can Create User`(){
        `when`(usersRepository.save(any(User::class.java))).thenReturn(user)
        val actual = userService.createUser("userTest","UserTestPassword")
        assertThat(actual).usingRecursiveComparison().isEqualTo(user)
        verify(usersRepository, times(1)).save(any(User::class.java))
        verifyNoMoreInteractions(usersRepository);
    }

    fun `User creation and retrieval tests`(): Unit {

        //Try to create user with unsafepassword
        var userResult = userService.createUser("testname","Password123")
        assertTrue(userResult is UserCreationInfo.UnsafePassword, "User was created with an unsafe password")

        //Try to add a valid user
        userResult = userService.createUser("user1","IamUser1234")
        assertTrue(userResult is UserCreationInfo.UserCreated, "User insert valid credentials but was not able to signUp")
        val id = userResult.userID

        //Try to create user with same username
        userResult = userService.createUser("user1","IamNotTheSameUser1234")
        assertTrue(userResult is UserCreationInfo.UserAlreadyExists, "A user with an already existent username was created")

        //Try to find user by wrong ID
        var getUserByID = userService.getUserById(UUID.randomUUID())
        assertTrue(getUserByID is GetUserResult.UserNotFound, "Found nonexistent user by id")

        //Try to find user by ID
        getUserByID = userService.getUserById(id)
        assertTrue(getUserByID is GetUserResult.UserFound)
        assertEquals("user1", getUserByID.user.username, "did not find existent user by id")

        //Try to find user by wrong username
        var getUserByName = userService.getUserByUsername("user2")
        assertTrue(getUserByName is GetUserResult.UserNotFound, "Found nonexistent user by username")

        //Try to find user by username
        getUserByName = userService.getUserByUsername("user1")
        assertTrue(getUserByName is GetUserResult.UserFound)
        assertEquals("user1", getUserByID.user.username, "did not find existent user")
    }


    fun `test token creation and get user by token`(){
        val userResult = userService.createUser("user1","IamUser1234")

        //Try to create token with wrong credentials
        var tokenCreationInfo = userService.createToken("user1", "abc")
        assertTrue(tokenCreationInfo is TokenCreationInfo.AuthenticationFailed, "received a token even though the credentials were not valid")

        //Try to create token with correct credentials
        tokenCreationInfo = userService.createToken("user1", "IamUser1234")
        assertTrue(tokenCreationInfo is TokenCreationInfo.TokenCreated, "failed to create a token with valid credentials")

        //Try to get user using an invalid token
        var getUser = userService.getUserByToken("not_a_token")
        assertTrue(getUser is GetUserResult.UserNotFound, "found a user using a nonexistent token")

        //Try to get user using a valid token
        getUser = userService.getUserByToken(tokenCreationInfo.token.id!!)
        assertTrue(getUser is GetUserResult.UserFound, "failed to find a user using a valid token")

        //Create two more tokens to exceed maximumNumber
        val firstToken = tokenCreationInfo.token.id
        userService.createToken("user1", "IamUser1234")
        userService.createToken("user1", "IamUser1234")

        //First Token is still valid
        getUser = userService.getUserByToken(firstToken!!)
        assertTrue(getUser is GetUserResult.UserFound)

        //Create a fourth token to delete the first
        userService.createToken("user1", "IamUser1234")

        getUser = userService.getUserByToken(firstToken)
        assertTrue(getUser is GetUserResult.UserNotFound)
    }


}