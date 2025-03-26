package com.example.widyaaksara.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.239.139:8000/api/"

    // Tambahkan Logging untuk melihat request dan response di Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log request dan response secara detail
    }

    // Tambahkan OkHttpClient dengan interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Gunakan Gson dengan setLenient()
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Pakai Gson yang sudah setLenient()
            .client(client) // Gunakan client dengan logging
            .build()
            .create(ApiService::class.java)
    }
}
