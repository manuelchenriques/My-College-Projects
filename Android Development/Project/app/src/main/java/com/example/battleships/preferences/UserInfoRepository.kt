package com.example.battleships.preferences

import com.example.battleships.model.Stat

interface UserInfoRepository {
    var userInfo : UserInfo?
    var gameInfo : GameInfo?
    var statInfo : Stat?
}

data class UserInfo(val nick:String, val token:String){
    init {
        require(validateUserInfoParts(nick, token))
    }
}

data class GameInfo(val gameID:Int, val gameState: String ,val opponent:String?=null){
    init {
        require(validateGameInfoParts(gameID,gameState,opponent))
    }
}

/**
 * Returns a [UserInfo] instance with the received values or null, if those
 * values are invalid.
 */
fun userInfoOrNull(nick: String?, token: String?): UserInfo? =
    if (validateUserInfoParts(nick, token))
        UserInfo(nick!!, token!!)
    else
        null

/**
 * Returns a [GameInfo] instance with the received values or null, if those
 * values are invalid.
 */
fun gameInfoOrNull(gameID:Int,gameState:String?,user: String?): GameInfo? =
    if (validateGameInfoParts(gameID,gameState,user))
        GameInfo(gameID,gameState!!,user)
    else
        null
/**
 * Checks whether the received values are acceptable as [UserInfo]
 * instance fields.
 */
fun validateUserInfoParts(nick: String?, token: String?) =
    !(nick.isNullOrBlank() || token.isNullOrBlank())

/**
 * Checks whether the received values are acceptable as [GameInfo]
 * instance fields.
 */
fun validateGameInfoParts(gameID:Int,gameState:String?,user:String? ) =
    when (gameState){
        "P1W" -> false
        "P2W" -> false
        "WP" -> user == null
        "SP" -> true
        else -> !user.isNullOrBlank()
    } && gameID > 0 && !gameState.isNullOrBlank()