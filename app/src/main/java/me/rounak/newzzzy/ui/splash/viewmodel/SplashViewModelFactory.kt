package me.rounak.newzzzy.ui.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.rounak.newzzzy.ui.main.viewmodel.MainViewModel
import java.lang.IllegalArgumentException

class SplashViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((MainViewModel::class.java))) {
            return SplashViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}