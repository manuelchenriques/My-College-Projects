package com.example.battleships.views.lobby.waitRoom

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
import com.example.battleships.utils.viewModelInit
import com.example.battleships.views.lobby.setUp.SetUpGameActivity

class WaitRoomActivity : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val userPref by lazy {
        dependencies.userService.userRepo
    }

    private val viewModel by viewModels<WaitRoomViewModel> {
        viewModelInit {
            WaitRoomViewModel(dependencies.userService,dependencies.gameServices)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, WaitRoomActivity::class.java)
                startActivity(intent)
            }
        }
        fun setGame(res: GameImage){
            _game = res
        }

        fun setJoin(res:Boolean){
            _joined = res
        }

        private var _joined by mutableStateOf(false)
        val joined
            get() = _joined

        private var _game by mutableStateOf<GameImage?>(null)
        val game
            get() = _game

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaitRoomScreen(
                joined = joined,
                userPref = userPref,
                game = game,
                proceedToSetUp = ::proceedToSetUp,
                waitForOpponent = ::waitForOpponent,
                getOpponent = ::getOpponentUsername,
                quitQueue = ::surrenderGame
            )
        }
    }

    private fun proceedToSetUp(){
        SetUpGameActivity.setGame(game)
        SetUpGameActivity.setPlayer1(!joined)
        SetUpGameActivity.navigate(this)
    }

    private fun waitForOpponent(){
        viewModel.checkForOpponent(userPref.userInfo!!.token, game!!.id,userPref)
    }

    private fun getOpponentUsername(){
        viewModel.getOpponentName(game!!.player1.toString(),userPref)
    }

    private fun surrenderGame(){
        viewModel.quitQueue(userPref,this)
    }

}