package com.example.battleships.views.lobby.playGame

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
import com.example.battleships.model.GameImage
import com.example.battleships.model.PlayInputModel
import com.example.battleships.utils.viewModelInit
import com.example.battleships.views.main.MainActivity

class PlayGameActivity : ComponentActivity(){

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private val viewModel by viewModels<PlayGameViewModel> {
        viewModelInit {
            PlayGameViewModel(dependencies.gameServices)
        }
    }

    companion object {
        fun navigate(context: Context){
            with(context){
                val intent = Intent(this, PlayGameActivity::class.java)
                startActivity(intent)
            }
        }
        fun setGame(res: GameImage?){
            _game = res
        }

        fun setPlayer1(res:Boolean){
            _player1 = res
        }

        private var _player1 by mutableStateOf(false)
        val player1
            get() = _player1

        private var _game by mutableStateOf<GameImage?>(null)
        val game
            get() = _game
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayGameScreen(
                game = game,
                user = userPref.userInfo?.nick,
                opponent = userPref.gameInfo?.opponent,
                onQuit = ::quitGame,
                checkGameState = ::checkGameState,
                isPlayer1 = player1,
                makeShot = ::makeShot,
                goBack = ::goBack
            )
        }
    }

    private fun quitGame(){
        viewModel.surrenderGame(userPref)

    }

    private fun checkGameState(){
        viewModel.checkGameState(userPref)
    }

    private fun makeShot(shot:PlayInputModel){
        viewModel.makeShot(shot, userPref)
    }

    private fun goBack(){
        userPref.gameInfo = null
        MainActivity.navigate(this)
    }

}