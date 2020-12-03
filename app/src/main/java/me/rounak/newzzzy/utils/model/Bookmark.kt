package me.rounak.newzzzy.utils.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_database")
data class Bookmark(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "author")
    var author: String = "",

    @ColumnInfo(name = "content")
    var content: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "publishedAt")
    var publishedAt: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "url")
    var url: String = "",

    @ColumnInfo(name = "urlToImage")
    var urlToImage: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "sourceName")
    var sourceName: String = ""

)