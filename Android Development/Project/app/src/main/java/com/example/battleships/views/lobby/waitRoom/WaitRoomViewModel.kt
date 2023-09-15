package com.example.battleships.views.lobby.waitRoom

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.GameImage
import com.example.battleships.model.User
import com.example.battleships.model.convert
import com.example.battleships.preferences.GameInfo
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.GameServices
import com.example.battleships.services.UserServices
import com.example.battleships.utils.hypermedia.SirenModel
import com.example.battleships.views.main.MainActivity
import kotlinx.coroutines.launch

class WaitRoomViewModel(
    private val userService: UserServices,
    private val gameServices: GameServices
) : ViewModel() {

    private var _gameImage by mutableStateOf<GameImage?>(null)
    private val gameImage
        get() = _gameImage

    private var _gameId by mutableStateOf(0)
    private val gameId
        get() = _gameId

    private var _opponent by mutableStateOf<SirenModel<User>?>(null)
    private val opponent
        get() = _opponent

    fun checkForOpponent(token: String, id: Int, userPref: UserInfoRepository) {
        viewModelScope.launch {
            _gameImage = try {
                Result.success(gameServices.getGameById(token,id).properties.convert()).getOrNull()
            }catch (e:Exception){
                null
            }
        }
        if (gameImage != null && gameImage!!.gameState != "WP"){
            WaitRoomActivity.setGame(gameImage!!)
            getOpponentName(gameImage!!.player2!!.toString(), userPref)
        }
    }

    fun getOpponentName(uuid: String, userPref: UserInfoRepository){
        viewModelScope.launch {
            _opponent = try {
                Result.success(userService.getUser(uuid)).getOrNull()
            }
            catch (e:Exception){
                null
            }
            if (opponent != null){
                val aux = userPref.gameInfo!!
                userPref.gameInfo = GameInfo(aux.gameID,"SP",opponent!!.properties.username)
            }
        }
    }

    fun quitQueue(userPref:UserInfoRepository,context: Context) {
        viewModelScope.launch {
            _gameId = try {
                Result.success(gameServices.surrenderGame(userPref.userInfo!!.token,userPref.gameInfo!!.gameID)).getOrNull().let { it ?: 0 }
            }
            catch (e:Exception){
                0
            }
            if (gameId == 0){
                throw IllegalStateException("OOF")
            }
            else{
                userPref.gameInfo = null
                MainActivity.navigate(context = context)
            }
        }
    }
}
