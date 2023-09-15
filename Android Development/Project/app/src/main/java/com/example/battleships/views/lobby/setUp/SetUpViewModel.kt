package com.example.battleships.views.lobby.setUp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.GameImage
import com.example.battleships.model.PlacementsInputModel
import com.example.battleships.model.convert
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.GameServices
import com.example.battleships.services.UserServices
import com.example.battleships.services.UtilityServices
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.views.main.MainActivity
import kotlinx.coroutines.launch

class SetUpViewModel(
    private val gameServices: GameServices
): ViewModel() {

    private var _gameImage by mutableStateOf<GameImage?>(null)
    private val gameImage
        get() = _gameImage

    fun endSetUp(map: PlacementsInputModel, userPref:UserInfoRepository) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.placeShips(map,userPref.userInfo!!.token,userPref.gameInfo!!.gameID).properties.convert()).getOrNull()
            }
            catch (e:Exception){
                null
            }
            if (gameImage != null){
                readyUp(userPref)
            }
            else{
                throw IllegalStateException("OOF")
            }
        }
    }

    private fun readyUp(userPref:UserInfoRepository){
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.readyUp(userPref.userInfo!!.token,userPref.gameInfo!!.gameID).properties.convert()).getOrNull()
            }
            catch (e:Exception){
                null
            }
            if (gameImage != null){
                SetUpGameActivity.setGame(gameImage!!)
            }
            else{
                throw IllegalStateException("OOF")
            }
        }

    }

    fun surrenderGame(userPref:UserInfoRepository, context: Context) {
        viewModelScope.launch {
            try {
                Result.success(gameServices.surrenderGame(userPref.userInfo!!.token,userPref.gameInfo!!.gameID))
            }
            catch (e:Exception){
                throw e
            }
        }
        SetUpGameActivity.setGame(gameImage!!)
        userPref.gameInfo = null
        MainActivity.navigate(context)
    }

    fun checkGameState(userPref:UserInfoRepository) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.getGameById(userPref.userInfo!!.token,userPref.gameInfo!!.gameID).properties.convert()).getOrNull()
            }catch (e:Exception){
                null
            }
        }
        if (gameImage != null && gameImage!!.gameState != "WP"){
            SetUpGameActivity.setGame(gameImage!!)
        }

    }

}