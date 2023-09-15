package com.example.battleships.views.utility.about

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
import com.example.battleships.model.AboutInfo
import com.example.battleships.utils.viewModelInit

class AboutActivity  : ComponentActivity() {

    private val dependencies by lazy {
        application as DependenciesContainer
    }

    private val viewModel by viewModels<AboutViewModel>{
        viewModelInit {
            AboutViewModel(dependencies.utilService)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }

        fun setInfo(res: Result<AboutInfo>?) {
            _info = res
        }

        private var _info by mutableStateOf<Result<AboutInfo>?>(null)
        val info
            get() = _info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutScreen(
                onReturn = ::goBack,
                aboutInfo = info?.getOrNull(),
                getAbout = ::getAbout
            )
        }
    }

    private fun getAbout(){
        viewModel.getAboutInfo()
    }

    private fun goBack(){
        MainActivity.navigate(this)
    }



}