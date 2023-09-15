package com.example.battleships.views.lobby.playGame

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.GameResponse
import com.example.battleships.model.PlayInputModel
import com.example.battleships.model.convert
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.GameServices
import com.example.battleships.services.UserServices
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.views.main.MainActivity
import kotlinx.coroutines.launch

class PlayGameViewModel(
    private val gameServices: GameServices
) : ViewModel() {

    private var _gameImage by mutableStateOf<SirenModel<GameResponse>?>(null)
    private val gameImage
        get() = _gameImage

    fun surrenderGame(userPref: UserInfoRepository) {
        viewModelScope.launch {
            try {
                Result.success(gameServices.surrenderGame(userPref.userInfo!!.token,userPref.gameInfo!!.gameID)).getOrNull()
            }
            catch (e:Exception){
                throw e
            }
        }
    }

    fun checkGameState(userPref: UserInfoRepository) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.getGameById(userPref.userInfo!!.token,userPref.gameInfo!!.gameID)).getOrNull()
            }catch (e:Exception){
                null
            }
        }
        val aux = gameImage?.properties?.convert()
        if (aux != null && aux.gameState != "WP"){
            PlayGameActivity.setGame(aux)
        }
    }

    fun makeShot(shot: PlayInputModel, userPref: UserInfoRepository) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.makeShot(userPref.userInfo!!.token,userPref.gameInfo!!.gameID,shot)).getOrNull()
            }catch (e:Exception){
                null
            }
        }
    }

}