package me.rounak.newzzzy.ui.bookmark.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.rounak.newzzzy.data.repository.NewsRepository
import me.rounak.newzzzy.utils.model.Bookmark

class BookmarkViewModel(private val repository: NewsRepository) : ViewModel(), Observable {

    fun insertBookmark(bookmark: Bookmark) = viewModelScope.launch {
        repository.insertBookmark(bookmark)
    }

    fun deleteBookmark(bookmark: Bookmark) = viewModelScope.launch {
        repository.deleteBookmark(bookmark)
    }

    fun deleteAllBookmarks() = viewModelScope.launch {
        repository.deleteAllBookmarks()
    }

    val bookmarks = repository.bookmarks

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}
