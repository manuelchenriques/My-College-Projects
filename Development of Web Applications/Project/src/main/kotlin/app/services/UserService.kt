package app.services

import app.jpa.entities.User
import java.util.*
interface UserService {
    fun createUser(username: String, password: String): UserCreationInfo
    fun getUserByUsername(username: String): GetUserResult
    fun getUserById(id: UUID): GetUserResult
    fun getUserByToken(token: String): GetUserResult
    fun createToken(username: String, password: String): TokenCreationInfo
    fun getUserStats(user : User) : GetUserStatsInfo
    fun getLeaderBoard(orderBy : UserServiceImpl.OrderBy, top: Int, idx: Int ,name: String?): GetUserStatsInfo

    fun clear(): Boolean
}