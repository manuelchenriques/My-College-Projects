package com.example.battleships.services

import android.util.Log
import com.example.battleships.model.Stat
import com.example.battleships.model.User
import com.example.battleships.model.UserToken
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.utils.hypermedia.takePropertiesFromBody
import com.example.battleships.utils.send
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.CacheControl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.lang.reflect.Type
import java.net.URL

enum class Mode { FORCE_REMOTE, FORCE_LOCAL, AUTO }

class UserServices(
    val userHome: URL,
    val userRepo: UserInfoRepository,
    val httpClient: OkHttpClient,
    private val jsonEncoder: Gson
) {
    suspend fun createUser(username:String,password:String,mode: Mode):SirenModel<User>{
        val request = createBuildRequest(userHome,mode, "{\"username\":\"$username\",\"passwordinfo\":\"$password\"}")

        val user = request.send(httpClient){ rsp ->
            val type = object:TypeToken<SirenModel<User>>(){}.type
            handleResponse<SirenModel<User>>(rsp,type)
        }
        return user
    }

    suspend fun logInUser(username:String,password:String,mode: Mode): UserToken {
        val signInURL = URL(userHome.toString().plus("/signIn"))
        val request = createBuildRequest(signInURL,mode,"{\"username\":\"$username\",\"passwordinfo\":\"$password\"}")

        val token = request.send(httpClient){ rsp ->
            val type = object:TypeToken<UserToken>(){}.type
            handleResponse<UserToken>(rsp,type)
        }
        return token
    }

    suspend fun getUserStats(token:String):SirenModel<Stat>{
        val statsURL = URL(userHome.toString().plus("/stats"))
        val request = createGetRequest(statsURL,token)

        val stats = request.send(httpClient){ rsp ->
            val type = object:TypeToken<SirenModel<Stat>>(){}.type
            handleResponse<SirenModel<Stat>>(rsp,type)
        }
        return stats
    }

    suspend fun getUser(uuid: String): SirenModel<User> {
        val userURL = URL(userHome.toString().plus("/$uuid"))
        val request= createGetWithoutToken(userURL)
        val user = request.send(httpClient){ rsp ->
            val type = object:TypeToken<SirenModel<User>>(){}.type
            handleResponse<SirenModel<User>>(rsp,type)
        }
        return user
    }

    private fun <T> handleResponse(response: Response, type: Type): T {
        val contentType = response.body?.contentType()
        return if (response.isSuccessful && contentType != null) {
            try {
                val body = response.body?.string()
                //val properties = takePropertiesFromBody(body,type)
                jsonEncoder.fromJson<T>(body, type)
            }
            catch (e: JsonSyntaxException) {
                throw UnexpectedResponseException(response)
            }
        }
        else {
            throw UnexpectedResponseException(response = response)
        }
    }

    private fun createGetWithoutToken(url: URL) =
        Request.Builder()
            .url(url)
            .header("Connection","keep-alive")
            .addHeader("Content-Type","application/json")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .build()

    private fun createGetRequest(url: URL,token: String) =
        Request.Builder()
            .url(url)
            .header("Connection","keep-alive")
            .addHeader("Content-Type","application/json")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Authorization","Bearer $token")
            .build()

    private fun createBuildRequest(url: URL, mode: Mode, reqBody:String) =
        with(Request.Builder()){
            when(mode){
                Mode.FORCE_REMOTE -> cacheControl(CacheControl.FORCE_NETWORK)
                Mode.FORCE_LOCAL -> cacheControl(CacheControl.FORCE_CACHE)
                else -> this
            }
        }.url(url)
            .header("Connection","keep-alive")
            .addHeader("Content-Type","application/json")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .post(reqBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()


    class UnresolvedLinkException(msg: String = "") : ApiException(msg)
    abstract class ApiException(msg: String) : Exception(msg)
    class UnexpectedResponseException(
        val response: Response? = null
    ) : ApiException("Unexpected ${response?.code} response from the API.")
}