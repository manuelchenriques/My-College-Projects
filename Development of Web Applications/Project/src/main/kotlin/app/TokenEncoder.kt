package app

import app.userDomain.TokenValidationInfo
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*

@Component
class TokenEncoder {

    fun createValidationInformation(token: String): TokenValidationInfo =
        TokenValidationInfo(hash(token))

    fun validate(validationInfo: TokenValidationInfo, token: String): Boolean =
        validationInfo.validationInfo == hash(token)

    private fun hash(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA256")
        return Base64.getUrlEncoder().encodeToString(
            messageDigest.digest(
                Charsets.UTF_8.encode(input).array()
            )
        )
    }

}