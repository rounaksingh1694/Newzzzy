package me.rounak.newzzzy.utils.model


import com.google.gson.annotations.SerializedName

data class NewsResult(
    @SerializedName("articles")
    val articles: ArrayList<NewsArticle>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)