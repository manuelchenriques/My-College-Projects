package com.example.battleships.preferences

import android.content.Context
import com.example.battleships.model.Stat

class UserInfoRepositorySharedPrefs(private val context: Context):UserInfoRepository {
    private val userNickKey = "Nick"
    private val userTokenKey = "Token"
    private val statIdKey = "Stat"
    private val statWinKey = "Wins"
    private val statPlayedKey = "Played"


    private val gameIDKey = "Id"
    private val gameStateKey = "State"
    private val gameUser = "Opponent"

    private val userPrefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    private val gamePrefs by lazy {
        context.getSharedPreferences("GameInfoPrefs", Context.MODE_PRIVATE)
    }

    override var statInfo :Stat?
        get() {
            val savedStatId = userPrefs.getInt(statIdKey,0)
            val savedStatWin = userPrefs.getInt(statWinKey,0)
            val savedStatPlayed = userPrefs.getInt(statPlayedKey,0)
            val savedNick = userPrefs.getString(userNickKey,null)
            return if (savedStatId > 0 && !savedNick.isNullOrBlank()) Stat(savedStatId,savedNick,savedStatPlayed,savedStatWin) else null
        }

        set(value) {
            if (value == null)
                userPrefs.edit()
                    .remove(statIdKey)
                    .remove(statPlayedKey)
                    .remove(statWinKey)
                    .apply()
            else
                userPrefs.edit()
                    .putInt(statIdKey, value.id)
                    .putInt(statWinKey, value.gamesWon)
                    .putInt(statPlayedKey, value.gamesPlayed)
                    .apply()
        }

    override var userInfo: UserInfo?
        get() {
            val savedNick = userPrefs.getString(userNickKey, null)
            val savedToken = userPrefs.getString(userTokenKey,null)
            return userInfoOrNull(savedNick,savedToken)
        }

        set(value) {
            if (value == null)
                userPrefs.edit()
                    .remove(userNickKey)
                    .remove(userTokenKey)
                    .apply()
            else
                userPrefs.edit()
                    .putString(userNickKey, value.nick)
                    .putString(userTokenKey, value.token)
                    .apply()
        }
    override var gameInfo: GameInfo?
        get() {
            val savedGame = gamePrefs.getInt(gameIDKey,0)
            val savedState = gamePrefs.getString(gameStateKey,null)
            val savedOpponent = gamePrefs.getString(gameUser,null)
            return gameInfoOrNull(savedGame,savedState,savedOpponent)
        }
        set(value) {
            if (value == null)
                gamePrefs.edit()
                    .remove(gameIDKey)
                    .remove(gameStateKey)
                    .remove(gameUser)
                    .apply()
            else
                gamePrefs.edit()
                    .putInt(gameIDKey,value.gameID)
                    .putString(gameStateKey,value.gameState)
                    .putString(gameUser,value.opponent)
                    .apply()
        }
}