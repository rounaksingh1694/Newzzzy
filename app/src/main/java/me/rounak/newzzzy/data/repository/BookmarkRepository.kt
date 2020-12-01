package me.rounak.newzzzy.data.repository

import me.rounak.newzzzy.data.local.BookmarkDAO
import me.rounak.newzzzy.utils.model.Bookmark

class BookmarkRepository(private val dao: BookmarkDAO) {

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

}