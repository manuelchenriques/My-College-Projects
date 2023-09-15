package com.example.battleships

import android.app.Application
import com.example.battleships.preferences.UserInfoRepositorySharedPrefs
import com.example.battleships.services.GameServices
import com.example.battleships.services.UserServices
import com.example.battleships.services.UtilityServices
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.net.URL

const val TAG = "BattleShipsApp"

interface DependenciesContainer {
    val userService:UserServices
    val utilService:UtilityServices
    val gameServices: GameServices
}

private const val baseStr = "https://e1cf-2001-818-d8eb-2a00-f7ee-7372-ed11-2abe.eu.ngrok.io"

private val userURL = URL("${baseStr}/user")
private val gamesURL = URL("${baseStr}/game")
private val baseURL = URL(baseStr)
class BattleShipsApplication : DependenciesContainer,Application() {
    private val userInfoRepo by lazy {
        UserInfoRepositorySharedPrefs(this)
    }
    private val httpClient by lazy {
        OkHttpClient.Builder()
            .cache(Cache(directory = cacheDir, maxSize = 50 * 1024 * 1024))
            .build()
    }
    override val gameServices:GameServices
        get() = GameServices(
            httpClient = httpClient,
            jsonEncoder = Gson(),
            gamesHome = gamesURL
        )

    override val userService: UserServices
        get() = UserServices(
            httpClient = httpClient,
            userRepo = userInfoRepo,
            jsonEncoder = Gson(),
            userHome = userURL
        )
    override val utilService: UtilityServices
        get() = UtilityServices(
            httpClient = httpClient,
            jsonEncoder = Gson(),
            baseHome = baseURL
        )
}