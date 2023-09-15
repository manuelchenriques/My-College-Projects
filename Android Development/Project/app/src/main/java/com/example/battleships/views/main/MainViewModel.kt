package com.example.battleships.views.main

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.*
import com.example.battleships.preferences.GameInfo
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.GameServices
import com.example.battleships.services.UserServices
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.views.lobby.waitRoom.WaitRoomActivity
import com.example.battleships.views.users.userProfile.UserProfileActivity
import kotlinx.coroutines.launch

class MainViewModel(
    private val gameServices: GameServices
): ViewModel() {

    private var _gameImage by mutableStateOf<SirenModel<GameResponse>?>(null)
    private val gameImage
        get() = _gameImage

    fun createOrJoinGame(userPref:UserInfoRepository, context: Context) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.joinOrCreateGame(userPref.userInfo!!.token)).getOrNull()
            }catch (e:Exception){
                null
            }
            if (gameImage != null) {
                val aux = gameImage!!.properties.convert()
                WaitRoomActivity.setGame(aux)
                userPref.gameInfo = GameInfo(aux.id,aux.gameState,null)
                WaitRoomActivity.setJoin(aux.gameState == "SP")
                WaitRoomActivity.navigate(context)
            }
        }
    }

}