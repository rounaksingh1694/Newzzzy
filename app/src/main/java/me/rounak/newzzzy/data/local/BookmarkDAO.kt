package me.rounak.newzzzy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.rounak.newzzzy.utils.model.Bookmark

@Dao
interface BookmarkDAO {

    @Insert
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmark_database")
    suspend fun deleteAllBookmarks()

    @Query("SELECT * FROM bookmark_database")
    fun getAllBookmarks(): LiveData<List<Bookmark>>

}