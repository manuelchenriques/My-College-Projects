package com.example.battleships.views.users.userProfile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.views.main.MainActivity
import com.example.battleships.model.Stat
import com.example.battleships.model.UserToken
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.UserServices
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userServices: UserServices
):ViewModel() {

    private var _stat by mutableStateOf<Result<Stat>?>(null)
    private val stats
        get() = _stat


    fun refreshUserStats(userPref: UserInfoRepository) {
        viewModelScope.launch {
            _stat = try {
                Result.success(userServices.getUserStats(userPref.userInfo!!.token).properties)
            } catch (e:Exception){
                Result.failure(e)
            }
            if (stats?.getOrNull() != null){
               userPref.statInfo = stats!!.getOrNull()
            }
        }
    }

}