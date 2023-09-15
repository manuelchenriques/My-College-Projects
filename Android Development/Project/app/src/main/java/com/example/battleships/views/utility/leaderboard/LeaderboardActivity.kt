package com.example.battleships.views.utility.leaderboard

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
import com.example.battleships.views.main.MainActivity
import com.example.battleships.model.Stat
import com.example.battleships.services.UtilityServices
import com.example.battleships.utils.viewModelInit

class LeaderboardActivity  : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val viewModel by viewModels<LeaderboardViewModel>{
        viewModelInit {
            LeaderboardViewModel(dependencies.utilService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
        }

        fun setRankings(res: List<Stat>?) {
            _ranks = res
        }

        fun setPage(next:Boolean,previous: Boolean){
            _hasNext = next
            _hasPrevious = previous
        }

        private var _ranks by mutableStateOf<List<Stat>?>(null)
        val ranks
            get() = _ranks

        private var _hasNext by mutableStateOf(false)
        val hasNext
            get() = _hasNext

        private var _hasPrevious by mutableStateOf(false)
        val hasPrevious
            get() = _hasPrevious
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeaderBoardScreen(
                onReturn = ::goBack,
                leaderBoard = ranks,
                next = viewModel.hasNext,
                previous = viewModel.hasPrevious,
                refreshLeaderBoard = ::refreshLeaderBoard
            )
        }
    }

    private fun refreshLeaderBoard(orderBy : UtilityServices.OrderBy, top: Int, idx: Int, name: String?){
        viewModel.getLeaderBoard(orderBy,top,idx,name)
    }

    private fun goBack(){
        MainActivity.navigate(this)
    }
}