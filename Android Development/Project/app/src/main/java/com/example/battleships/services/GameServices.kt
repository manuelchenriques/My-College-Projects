package com.example.battleships.services

import android.util.Log
import com.example.battleships.model.*
import com.example.battleships.model.gameDomain.ShipType
import com.example.battleships.utils.hypermedia.SirenModel
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


private val defaultGame = GameCreationInputModel(
    boardX = 10,
    boardY = 10,
    fleetschema = hashMapOf(
        "Carrier" to 1,
        "Battleship" to 1,
        "Cruiser" to 1,
        "Submarine" to 1,
        "Destroyer" to 1
    ),
    shotsperround = 1,
    setupTime = 300000,
    roundtime = 180000
)

class GameServices(
    val httpClient: OkHttpClient,
    val gamesHome: URL,
    private val jsonEncoder:Gson
) {

    suspend fun getGameById(token: String, id: Int): SirenModel<GameResponse> {
        val getURL = URL(gamesHome.toString().plus("/${id}"))
        val request = createGetRequest(getURL, token)

        val gameImage = request.send(httpClient) { rsp ->
            val type = object : TypeToken<SirenModel<GameResponse>>() {}.type
            handleResponse<SirenModel<GameResponse>>(rsp, type)
        }
        return gameImage
    }

    suspend fun joinOrCreateGame(token: String?): SirenModel<GameResponse> {
        val fastURL = URL(gamesHome.toString().plus("/fast"))
        val request = createBuildRequest(fastURL, Mode.AUTO, jsonEncoder.toJson(defaultGame), token)

        val gameImage = request.send(httpClient) { rsp ->
            val type = object : TypeToken<SirenModel<GameResponse>>() {}.type
            handleResponse<SirenModel<GameResponse>>(rsp, type)
        }
        return gameImage
    }

    suspend fun surrenderGame(token: String, id: Int): Int {
        val surrenderURL = URL(gamesHome.toString().plus("/${id}/surrender"))
        val request = createBuildRequest(surrenderURL, Mode.AUTO, "", token)

        val gameID = request.send(httpClient) { rsp ->
            val type = object : TypeToken<Int>() {}.type
            handleResponse<Int>(rsp, type)
        }
        return gameID
    }

    suspend fun placeShips(inputModel: PlacementsInputModel, token: String, id: Int): SirenModel<GameResponse> {
        val fastURL = URL(gamesHome.toString().plus("/${id}/ship"))
        val request = createBuildRequest(fastURL, Mode.AUTO, jsonEncoder.toJson(inputModel), token)

        val gameImage = request.send(httpClient) { rsp ->
            val type = object : TypeToken<SirenModel<GameResponse>>() {}.type
            handleResponse<SirenModel<GameResponse>>(rsp, type)
        }
        return gameImage
    }

    suspend fun readyUp(token: String, id: Int): SirenModel<GameResponse> {
        val fastURL = URL(gamesHome.toString().plus("/${id}/ready"))
        val request = createBuildRequest(fastURL, Mode.AUTO, "", token)

        val gameImage = request.send(httpClient) { rsp ->
            val type = object : TypeToken<SirenModel<GameResponse>>() {}.type
            handleResponse<SirenModel<GameResponse>>(rsp, type)
        }
        return gameImage
    }

    suspend fun makeShot(token: String, id: Int, shot: PlayInputModel): SirenModel<GameResponse> {
        val shotURL = URL(gamesHome.toString().plus("/${id}/play"))
        val request = createBuildRequest(shotURL, Mode.AUTO, jsonEncoder.toJson(shot), token)
        val gameImage = request.send(httpClient) { rsp ->
            val type = object : TypeToken<SirenModel<GameResponse>>() {}.type
            handleResponse<SirenModel<GameResponse>>(rsp, type)
        }
        return gameImage
    }

    private fun <T> handleResponse(response: Response, type: Type): T {
        val contentType = response.body?.contentType()
        return if (response.isSuccessful && contentType != null) {
            try {
                val body = response.body?.string()

                jsonEncoder.fromJson<T>(body, type)

            } catch (e: JsonSyntaxException) {
                Log.e("On", e.message.toString())
                throw UserServices.UnexpectedResponseException(response)
            }
        } else {

            throw UserServices.UnexpectedResponseException(response = response)
        }
    }

    private fun createGetRequest(url: URL, token: String) =
        Request.Builder()
            .url(url)
            .header("Connection", "keep-alive")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Authorization", "Bearer $token")
            .build()

    private fun createBuildRequest(url: URL, mode: Mode, reqBody: String, token: String?) =
        with(Request.Builder()) {
            when (mode) {
                Mode.FORCE_REMOTE -> cacheControl(CacheControl.FORCE_NETWORK)
                Mode.FORCE_LOCAL -> cacheControl(CacheControl.FORCE_CACHE)
                else -> this
            }
        }.url(url)
            .header("Connection", "keep-alive")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Authorization", "Bearer $token")
            .post(reqBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

}