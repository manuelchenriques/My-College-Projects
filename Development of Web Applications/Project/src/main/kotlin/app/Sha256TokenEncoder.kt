package app

import app.userDomain.TokenValidationInfo
import java.security.MessageDigest
import java.util.Base64
class Sha256TokenEncoder   {

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
    }}