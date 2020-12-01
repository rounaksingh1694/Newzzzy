package me.rounak.newzzzy.ui.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.rounak.newzzzy.data.repository.BookmarkRepository

class BookmarkViewModelFactory(private val repository: BookmarkRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((BookmarkViewModel::class.java))) {
            return BookmarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}