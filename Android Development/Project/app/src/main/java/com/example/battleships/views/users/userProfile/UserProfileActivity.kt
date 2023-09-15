package com.example.battleships.views.users.userProfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleships.DependenciesContainer
import com.example.battleships.views.main.MainActivity
import com.example.battleships.views.users.signIn.SignInActivity
import com.example.battleships.utils.viewModelInit

class UserProfileActivity : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private val viewModel by viewModels<UserProfileViewModel>{
        viewModelInit {
            UserProfileViewModel(dependencies.userService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserProfileScreen(
                onReturn = ::goBack,
                onSignOut = ::signOut,
                onRefreshStats = ::refreshStats,
                userPref = userPref
            )
        }
    }

    private fun refreshStats(){
        if (userPref.userInfo != null)
            viewModel.refreshUserStats(userPref)
    }

    private fun goBack(){
        MainActivity.navigate(this)
    }

    private fun signOut(){
        userPref.userInfo = null
        userPref.statInfo = null
        SignInActivity.navigate(this)
    }
}