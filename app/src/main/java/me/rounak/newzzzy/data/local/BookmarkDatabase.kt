package me.rounak.newzzzy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.rounak.newzzzy.utils.model.Bookmark

@Database(entities = [Bookmark::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract val bookmarkDAO: BookmarkDAO

    companion object {

        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getInstance(context: Context): BookmarkDatabase {

            synchronized(this) {

                var instance = INSTANCE

                if (instance ==  null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarkDatabase::class.java,
                        "subscriber_data_database"
                    ).build()
                }

                return instance

            }

        }

    }

}