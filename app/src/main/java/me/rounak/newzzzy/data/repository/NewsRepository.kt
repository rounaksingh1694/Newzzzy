package me.rounak.newzzzy.data.repository

import com.rounak.newsapp.NewsService
import com.rounak.newsapp.RetrofitInstance
import me.rounak.newzzzy.data.local.BookmarkDAO
import me.rounak.newzzzy.utils.model.Bookmark

class NewsRepository (private val dao: BookmarkDAO) {

    suspend fun getAllBookmarks() = dao.getAllBookmarks()

    suspend fun insertBookmark(bookmark: Bookmark) {
        dao.insertBookmark(bookmark)
    }

    suspend fun deleteBookmark(bookmark: Bookmark) {
        dao.deleteBookmark(bookmark)
    }

    suspend fun deleteAllBookmarks() {
        dao.deleteAllBookmarks()
    }

    private val retrofitInstance = RetrofitInstance.getInstance().create(NewsService::class.java)

    suspend fun getNewsByCountryAndCategory(country: String, category: String) =
        retrofitInstance.getNewsByCountryAndCategory(country, category)

}
