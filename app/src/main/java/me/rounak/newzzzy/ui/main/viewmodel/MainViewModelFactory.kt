package me.rounak.newzzzy.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.rounak.newzzzy.data.repository.NewsRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((MainViewModel::class.java))) {
            return MainViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
