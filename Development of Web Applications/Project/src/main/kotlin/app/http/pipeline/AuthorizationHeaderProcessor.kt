package app.http.pipeline

import app.jpa.entities.User
import app.services.GetUserResult
import app.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderProcessor(
    val usersService: UserService
) {

    fun process(authorizationValue: String?): User? {
            if (authorizationValue == null) {
                return null
            }
            val parts = authorizationValue.trim().split(" ")
            if (parts.size != 2) {
                return null
            }
            if (parts[0].lowercase() != SCHEME) {
                return null
            }

            return when (val x = usersService.getUserByToken(parts[1])){
            is GetUserResult.UserFound -> x.user
            else -> {
                null
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderProcessor::class.java)
        const val SCHEME = "bearer"
    }
}