package me.rounak.newzzzy.utils.model


import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.Nullable


data class NewsSource(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
)