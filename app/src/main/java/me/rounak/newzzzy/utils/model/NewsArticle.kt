package me.rounak.newzzzy.utils.model


import com.google.gson.annotations.SerializedName

data class NewsArticle(
    @SerializedName("author")
    val author: String = "",
    @SerializedName("content")
    val content: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("publishedAt")
    val publishedAt: String = "",
    @SerializedName("source")
    val source: NewsSource,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("urlToImage")
    val urlToImage: String = ""
)