package com.example.battleships.services

import android.util.Log
import com.example.battleships.model.AboutInfo
import com.example.battleships.model.LeaderBoard
import com.example.battleships.model.Stat
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.utils.send
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.reflect.Type
import java.net.URI
import java.net.URL

class UtilityServices(
    val httpClient: OkHttpClient,
    val jsonEncoder: Gson,
    val baseHome: URL
) {

    suspend fun getAbout():AboutInfo{
        val aboutURL = URL(baseHome.toString().plus("/about"))
        val request = createGetRequest(aboutURL)

        val info = request.send(httpClient){ rsp ->
            val type = object:TypeToken<AboutInfo>(){}.type
            handleResponse<AboutInfo>(rsp,type)
        }
        return info
    }
    enum class OrderBy(value: String?) {
        Wins("Wins"),
        GamesPlayed("GamesPlayed"),
        Default(null)
    }
    suspend fun getRankings(orderBy : OrderBy, top: Int, idx: Int ,name: String?): SirenModel<LeaderBoard> {

        if (top < 1 || idx < 0 ) throw Exception("wrong headers ")
        val requestStringBuilder = "/leaderboard?orderBy=${orderBy.name}&top=${top}&idx=${idx}".plus(  if(name!= null)"&name=${name}" else "")
        Log.v("Request", requestStringBuilder)
        val rankingURL = URL(baseHome.toString().plus(requestStringBuilder))
        val request = createGetRequest(rankingURL)

        val ranks = request.send(httpClient){ rsp ->
            val type = object : TypeToken<SirenModel<LeaderBoard>>(){}.type

            handleResponse<SirenModel<LeaderBoard>>(rsp,type)
        }
        Log.v("RANKS",ranks.toString())
        return ranks
    }

    private fun <T> handleResponse(response: Response, type: Type): T {
        val contentType = response.body?.contentType()
        return if (response.isSuccessful && contentType != null) {
            try {
                jsonEncoder.fromJson<T>(response.body?.string(), type)
            }
            catch (e: JsonSyntaxException) {
                throw UnexpectedResponseException(response)
            }
        }
        else {
            throw UnexpectedResponseException(response = response)
        }
    }

    private fun createGetRequest(url: URL) =
        Request.Builder()
            .url(url)
            .header("Connection","keep-alive")
            .addHeader("Content-Type","application/json")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .build()

    class UnexpectedResponseException(
        val response: Response? = null
    ) : UserServices.ApiException("Unexpected ${response?.code} response from the API.")

}