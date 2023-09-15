package app.services

import app.Clock
import app.TokenEncoder
import app.jpa.entities.Token
import app.jpa.entities.User
import app.jpa.entities.UserStat
import app.jpa.repositories.TokensRepository
import app.jpa.repositories.UserStatsRepository
import app.jpa.repositories.UsersRepository
import app.userDomain.UserLogic
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserServiceImpl(
    private val tokensRepository: TokensRepository,
    private val usersRepository: UsersRepository,
    private val userStatsRepository: UserStatsRepository,
    private val userLogic: UserLogic,
    private val passEncoder: PasswordEncoder,
    private val tokenEncoder: TokenEncoder,
    private val clock: Clock
) : UserService {

    @Transactional
    override fun createUser(username: String, password: String): UserCreationInfo {
        if (!userLogic.isSafePassword(password)) return UserCreationInfo.UnsafePassword
        if (usersRepository.existsUserByUsername(username)) return UserCreationInfo.UserAlreadyExists

        val user = User(username, passEncoder.encode(password))
        usersRepository.save(user)
        return UserCreationInfo.UserCreated(user.id!!)
    }

    @Transactional(readOnly = true)
    override fun getUserByUsername(username: String): GetUserResult {
        val user = usersRepository.findUserByUsername(username) ?: return GetUserResult.UserNotFound
        return GetUserResult.UserFound(user)
    }

    @Transactional(readOnly = true)
    override fun getUserById(id: UUID): GetUserResult {
        val user = usersRepository.findUserById(id) ?: return GetUserResult.UserNotFound
        return GetUserResult.UserFound(user)
    }

    @Transactional(readOnly = true)
    override fun getUserByToken(token: String): GetUserResult {
        val tkn = tokensRepository.findTokenById(token) ?: return GetUserResult.AuthenticationFailed
        val user = usersRepository.findUserById(tkn.userid!!.id!!) ?: return GetUserResult.UserNotFound
        return GetUserResult.UserFound(user)
    }

    @Transactional(readOnly = true)
    override fun getUserStats(user: User): GetUserStatsInfo {
        return GetUserStatsInfo.UserStatFound(user.stats!!)
    }

    public enum class OrderBy(value: String?) {
        Wins("Wins"),
        GamesPlayed("gamesPlayed"),
        Default(null)
    }

    @Transactional(readOnly = true)
    override fun getLeaderBoard(orderBy: OrderBy, top: Int, idx: Int , name: String?): GetUserStatsInfo {
        println(orderBy)
        println(top)
        println(idx)

        if (top < 1 || idx < 0 ) return GetUserStatsInfo.WrongHeaders
        if(name != null && name !=""){
            val  userStat =  usersRepository.findUserByUsername(name)?.stats
            return GetUserStatsInfo.UserStatsFound(listOf(userStat),false,false)
        }
        val order= if(orderBy == OrderBy.Wins) "gamesWon" else orderBy.name

        val userStats = userStatsRepository.findAll(
            PageRequest.of(
                idx,
                top,
                Sort.by(Sort.Direction.DESC, order)
            )
        )

        println(userStats)
        return GetUserStatsInfo.UserStatsFound(userStats.content,userStats.hasNext(), userStats.hasPrevious())

    }

    @Transactional
    override fun createToken(username: String, password: String): TokenCreationInfo {
        if (username.isBlank() || password.isBlank()) return TokenCreationInfo.AuthenticationFailed
        val user = usersRepository.findUserByUsername(username)
        if (user == null || !passEncoder.matches(
                password,
                user.passwordinfo
            )
        ) return TokenCreationInfo.AuthenticationFailed
        val now = clock.now()

        if (tokensRepository.countTokenByUserid(user) >= 3) {
            val firstToken = tokensRepository.findTopByUseridOrderByCreatedAtAsc(user)
            tokensRepository.removeTokenById(firstToken!!.id!!)
        }

        val tkn =
            Token(tokenEncoder.createValidationInformation(userLogic.createToken()).validationInfo, user, now, now)
        tokensRepository.save(tkn)
        return TokenCreationInfo.TokenCreated(tkn)
    }

    override fun clear(): Boolean {
        tokensRepository.deleteAll()
        usersRepository.deleteAll()
        userStatsRepository.deleteAll()
        return true
    }

}

sealed class UserCreationInfo {
    object UnsafePassword : UserCreationInfo()
    object UserAlreadyExists : UserCreationInfo()
    data class UserCreated(val userID: UUID) : UserCreationInfo()
}

sealed class TokenCreationInfo {
    object AuthenticationFailed : TokenCreationInfo()
    data class TokenCreated(val token: Token) : TokenCreationInfo()
}

sealed class GetUserStatsInfo {

    data class UserStatsFound(val userStats: List<UserStat?>, val hasNext: Boolean, val hasPrev:Boolean) : GetUserStatsInfo()
    data class UserStatFound(val userStats: UserStat) : GetUserStatsInfo()
    object UserStatsInfoNotFound : GetUserStatsInfo()
    object WrongHeaders : GetUserStatsInfo()
}

sealed class GetUserResult {
    object UserNotFound : GetUserResult()
    object AuthenticationFailed : GetUserResult()
    data class UserFound(val user: User) : GetUserResult()
}
