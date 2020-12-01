package me.rounak.newzzzy.ui.main.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.rounak.newzzzy.data.repository.NewsRepository
import me.rounak.newzzzy.utils.model.Bookmark
import me.rounak.newzzzy.utils.model.NewsResult
import retrofit2.Response

class MainViewModel(private val newsRepository: NewsRepository) : ViewModel(), Observable {

    lateinit var newsResponse: LiveData<Response<NewsResult>>

    @Bindable
    val refreshStatus: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getNewsByCountryAndCategory(country: String, category: String) {

        viewModelScope.launch {

            refreshStatus.value = true
            Log.i("refreshStatus", refreshStatus.value.toString())



                newsResponse = liveData {

                    try {

                        val newsResult =
                            newsRepository.getNewsByCountryAndCategory(country, category)
                        Log.i("Articles", newsResult.toString())
                        emit(newsResult)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            refreshStatus.value = false
            Log.i("refreshStatus", refreshStatus.value.toString())

        }

    }

    fun insertBookmark(bookmark: Bookmark) = viewModelScope.launch {
        newsRepository.insertBookmark(bookmark)
    }

    fun deleteBookmark(bookmark: Bookmark) = viewModelScope.launch {
        newsRepository.deleteBookmark(bookmark)
    }

    fun deleteAllBookmarks() = viewModelScope.launch {
        newsRepository.deleteAllBookmarks()
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}
