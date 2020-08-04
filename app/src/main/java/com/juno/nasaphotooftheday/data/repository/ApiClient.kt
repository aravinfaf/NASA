package com.juno.nasaphotooftheday.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.juno.nasaphotooftheday.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    val  BASE_URL:String ="https://api.nasa.gov/"


    fun create(): ApiInterface {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()


        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(380, TimeUnit.SECONDS)
            .readTimeout(380, TimeUnit.SECONDS)
            .writeTimeout(380, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
            .create(ApiInterface::class.java)
    }
}