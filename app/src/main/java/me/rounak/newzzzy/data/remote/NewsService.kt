package com.rounak.newsapp

import me.rounak.newzzzy.utils.model.NewsResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/v2/top-headlines/")
    suspend fun getNewsByCountryAndCategory(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = RetrofitInstance.API_KEY
    ): Response<NewsResult>

    @GET("/v2/top-headlines/")
    suspend fun getNewsByCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = RetrofitInstance.API_KEY
    ): Response<NewsResult>

}
