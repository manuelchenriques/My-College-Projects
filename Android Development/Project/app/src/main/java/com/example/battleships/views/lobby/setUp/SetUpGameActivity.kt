package com.example.battleships.views.lobby.setUp

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
import com.example.battleships.model.gameDomain.Orientation
import com.example.battleships.model.gameDomain.Position
import com.example.battleships.model.gameDomain.ShipType
import com.example.battleships.model.toInputModel
import com.example.battleships.utils.viewModelInit
import com.example.battleships.views.lobby.playGame.PlayGameActivity
import com.example.battleships.views.main.MainActivity


class SetUpGameActivity  : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private val viewModel by viewModels<SetUpViewModel>{
        viewModelInit {
            SetUpViewModel(dependencies.gameServices)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, SetUpGameActivity::class.java)
                startActivity(intent)
            }
        }
        fun setGame(res:GameImage?){
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
            SetUpGameScreen(
                game = game,
                user = userPref.userInfo?.nick,
                opponent = userPref.gameInfo?.opponent,
                isPlayer1 = player1,
                onQuit = ::quitGame,
                endSetUp = ::endSetUp,
                startPlaying = ::startPlaying,
                checkGameState = ::checkGameState
            )
        }
    }

    private fun quitGame(state:String){
        if (state == "P1W" || state == "P2W"){
            MainActivity.navigate(this)
        }
        else {
            viewModel.surrenderGame(userPref, this)
        }
    }

    private fun endSetUp(map: Map<ShipType,Pair<Orientation,Position>>){
        viewModel.endSetUp(map.toInputModel(), userPref)
    }

    private fun checkGameState(){
        viewModel.checkGameState(userPref)
    }

    private fun startPlaying(){
        PlayGameActivity.setGame(game)
        PlayGameActivity.setPlayer1(player1)
        PlayGameActivity.navigate(this)
    }
}