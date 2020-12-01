package me.rounak.newzzzy.utils.model


import com.google.gson.annotations.SerializedName

data class NewsSource(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
)