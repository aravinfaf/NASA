package com.juno.nasaphotooftheday.data.repository

import com.juno.nasaphotooftheday.data.model.ApiModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("planetary/apod")
    public fun getData(@Query("api_key")apikey:String ): Observable<ApiModel>

    @GET("planetary/apod")
    fun getDataDate(@Query("api_key")apikey:String,@Query("date")date:String) : Observable<ApiModel>
}