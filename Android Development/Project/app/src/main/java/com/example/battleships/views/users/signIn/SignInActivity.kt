package com.example.battleships.views.users.signIn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.battleships.DependenciesContainer
import com.example.battleships.model.AuthResponses
import com.example.battleships.views.main.MainActivity
import com.example.battleships.utils.viewModelInit


class SignInActivity : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private val viewModel by viewModels<SignInViewModel>{
        viewModelInit {
            SignInViewModel(dependencies.userService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
        }

        fun setAuthResponse(res: AuthResponses?) {
            _authRes = res
        }

        private var _authRes by mutableStateOf<AuthResponses?>(null)
        val authRes
            get() = _authRes
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            SignInScreen(
                onRegister = ::sendUserRegister,
                onSignIn = ::sendUserLogIn,
                onReturn = ::goBack,
                userPref = userPref
            )
        }
    }
    private fun sendUserLogIn(username: String, password: String){
        viewModel.userLogIn(username,password,userPref)
    }

    private fun sendUserRegister(username: String, password: String) {
        viewModel.createUser(username,password)
    }
    private fun goBack(){
        setAuthResponse(null)
        MainActivity.navigate(this)
    }



}
