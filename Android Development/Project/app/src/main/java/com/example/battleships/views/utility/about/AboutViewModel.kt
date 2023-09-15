package com.example.battleships.views.utility.about

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.battleships.model.AboutInfo
import com.example.battleships.services.UtilityServices
import kotlinx.coroutines.launch

class AboutViewModel(
    private val utilService: UtilityServices
) : ViewModel() {

    private var _info by mutableStateOf<Result<AboutInfo>?>(null)
    private val info
        get() = _info

    fun getAboutInfo(){
        viewModelScope.launch {
            _info = try {
                Result.success(utilService.getAbout())
            } catch (e:Exception){
                Result.failure(e)
            }
            if (info?.getOrNull() != null){
                AboutActivity.setInfo(info)
            }
        }
    }

}