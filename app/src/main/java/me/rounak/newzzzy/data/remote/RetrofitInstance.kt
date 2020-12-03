package com.rounak.newsapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {

        val BASE_URL = "http://newsapi.org/"

        val interceptor = HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().apply { this.addInterceptor(interceptor) }.build()

        fun getInstance(): Retrofit =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

    }

}
