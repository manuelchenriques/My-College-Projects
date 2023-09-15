package com.example.battleships.views.utility.leaderboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleships.model.LeaderBoard
import com.example.battleships.services.UtilityServices
import com.example.battleships.utils.hypermedia.SirenModel
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val utilService: UtilityServices
) :ViewModel() {

    private var _ranks by mutableStateOf<Result<SirenModel<LeaderBoard>>?>(null)
    private val ranks
        get() = _ranks

    private var _hasNext by mutableStateOf(false)
    val hasNext
        get() = _hasNext

    private var _hasPrevious by mutableStateOf(false)
    val hasPrevious
        get() = _hasPrevious



    fun getLeaderBoard(orderBy : UtilityServices.OrderBy, top: Int, idx: Int, name: String?) {
        viewModelScope.launch {
            _ranks = try {
                Result.success(utilService.getRankings(orderBy,top,idx,name))

            } catch (e:Exception){
                Result.failure(e)
            }
            Log.v("RANKS",ranks.toString())
            if (ranks?.getOrNull() != null) {
                LeaderboardActivity.setRankings(ranks?.getOrNull()?.properties?.stats)
                _hasNext = false
                _hasPrevious = false
                ranks?.getOrNull()?.links?.forEach {
                    if(it.rel.contains("next"))
                        _hasNext = true
                    if(it.rel.contains("previous"))
                        _hasPrevious = true
                }
            }
            Log.v("Pages", "Previous: $hasPrevious & Next: $hasNext")
            LeaderboardActivity.setPage(hasNext,hasPrevious)

        }
    }
}