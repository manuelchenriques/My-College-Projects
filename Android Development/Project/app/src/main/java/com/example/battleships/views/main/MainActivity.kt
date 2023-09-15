package com.example.battleships.views.main

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
import com.example.battleships.utils.viewModelInit
import com.example.battleships.views.users.signIn.SignInActivity
import com.example.battleships.views.users.userProfile.UserProfileActivity
import com.example.battleships.views.utility.about.AboutActivity
import com.example.battleships.views.utility.leaderboard.LeaderboardActivity

class MainActivity : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val viewModel by viewModels<MainViewModel>{
        viewModelInit {
            MainViewModel(dependencies.gameServices)
        }
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private fun isUserLogged() = userPref.userInfo != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(
                onPlayGames = ::goToWaitingRoom,
                onGetLeaderboard = ::getLeaderboard,
                onGetAbout = ::getAbout,
                onSigIn = ::signIn,
                isUserLogged = isUserLogged(),
                username = userPref.userInfo?.nick
            )
        }
    }
    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun signIn(){
        if (isUserLogged()){
            UserProfileActivity.navigate(this)
        }else{
            SignInActivity.navigate(this)
        }
    }

    private fun goToWaitingRoom(){
        viewModel.createOrJoinGame(userPref,this)
    }

    private fun getLeaderboard(){
        LeaderboardActivity.navigate(this)
    }

    private fun getAbout(){
        AboutActivity.navigate(this)
    }


}
