package app.userDomain

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*
@Component
class UserLogic {

    //TODO("Token System")

    fun createToken(): String =
        ByteArray(TOKEN_BYTE_SIZE).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    fun canBeToken(token: String): Boolean = try {
        Base64.getUrlDecoder()
            .decode(token).size == TOKEN_BYTE_SIZE
    } catch (ex: IllegalArgumentException) {
        false
    }

    fun isSafePassword(password: String): Boolean {

        var hasCapital = false
        var hasLower = false
        var hasNumber = false

        for(i in password.indices){
           if (password[i].isUpperCase()) {
               hasCapital = true
           }
           if (password[i].isLowerCase()) {
               hasLower = true
           }
           if (password[i].isDigit()) {
               hasNumber = true
           }
        }

        return hasLower && hasCapital && hasNumber && password.length > 8
    }

    companion object {
        private const val TOKEN_BYTE_SIZE = 256 / 8
    }
}