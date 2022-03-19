package com.example.aceplussolution.service

import com.example.aceplussolution.common.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Phone Thet Khine (19.3.2022)
 * This is the Retrofit Object to use in the API Call.
 */
object RetrofitObj {
    val API_SERVICE: APIService = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(Interceptor.client)
        .build()
        .create(APIService::class.java)
}

object Interceptor {
    val client = OkHttpClient.Builder()
        .addInterceptor(run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        })
        .build()
}


