package me.rounak.newzzzy.utils.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_database")
data class Bookmark(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,

    @ColumnInfo
    val author: String = "",

    @ColumnInfo
    val content: String = "",

    @ColumnInfo
    val description: String = "",

    @ColumnInfo
    val publishedAt: String = "",

    @ColumnInfo
    val source: NewsSource,

    @ColumnInfo
    val title: String = "",

    @ColumnInfo
    val url: String = "",

    @ColumnInfo
    val urlToImage: String = "",

    @ColumnInfo
    val category: String = ""

)