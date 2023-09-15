package com.example.battleships.views.users.signIn

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.*
import com.example.battleships.preferences.UserInfo
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.services.Mode
import com.example.battleships.services.UserServices
import kotlinx.coroutines.launch


class SignInViewModel(
    private val userServices: UserServices
) :ViewModel(){

    private var _user by mutableStateOf<Result<User>?>(null)
    private val user
        get() = _user

    private var _token by mutableStateOf<Result<UserToken>?>(null)
    private val token
        get() = _token


    fun createUser(username: String, password: String){
        viewModelScope.launch {
            if (!validatePassword(password)){
                SignInActivity.setAuthResponse(AuthResponses.BadCredentials)
                return@launch
            }

            _user = try {
                Result.success(userServices.createUser(username,password,Mode.AUTO).properties)

            } catch (e:Exception){
                Result.failure(e)
            }
            if (user?.getOrNull() == null){
                Log.e("In Scope","Bad")
                SignInActivity.setAuthResponse(AuthResponses.RegisterFailed)
            }else{
                Log.v("In Scope","${user?.getOrNull()}")
                SignInActivity.setAuthResponse(AuthResponses.RegisterSuccessful)
            }
        }
    }

    fun userLogIn(username: String, password: String, userPref: UserInfoRepository) {
        viewModelScope.launch {
            if (!validatePassword(password)){
                SignInActivity.setAuthResponse(AuthResponses.BadCredentials)
                return@launch
            }

            _token = try {
                Result.success(userServices.logInUser(username,password,Mode.AUTO))
            } catch (e:Exception){
                Result.failure(e)
            }
            if (token?.getOrNull() == null){
                SignInActivity.setAuthResponse(AuthResponses.SignInFailed)
            }else{
                userPref.userInfo = UserInfo(username,token!!.getOrNull()!!.token)
                SignInActivity.setAuthResponse(AuthResponses.SignInSuccessful)
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        var hasCapital = false
        var hasLower = false
        var hasNumber = false

        for(i in password.indices){
            if (password[i].isUpperCase()) {
                hasCapital = true
            }
            if (password[i].isLowerCase()) {
                hasLower = true
            }
            if (password[i].isDigit()) {
                hasNumber = true
            }
        }

        return hasLower && hasCapital && hasNumber && password.length > 8
    }


}