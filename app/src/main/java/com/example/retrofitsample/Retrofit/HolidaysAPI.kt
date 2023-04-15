package com.example.retrofitsample.Retrofit

import com.example.retrofitsample.Model.Holiday
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val baseURL = "http://192.168.29.155:8080/api/"

private val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().baseUrl(baseURL)
    .addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface HolidaysAPIService {
    @GET("holidays")
    suspend fun getHolidays() :List<Holiday>

    @DELETE("holidays/{holidayName}")
    suspend fun deleteHoliday(@Path("holidayName") name :String) :Response<Unit>

    @POST("holidays")
    suspend fun postHoliday(@Body holiday: Holiday) :Response<Unit>

    @PATCH("holidays")
    suspend fun patchHoliday(@Body holiday: Holiday) : Response<Unit>

    @GET("holidays/{holidayName}")
    suspend fun getHolidayByName(@Path("holidayName") name: String) :Response<Holiday>

}


object HolidaysAPI{
    val retrofitService:HolidaysAPIService by lazy { retrofit.create(HolidaysAPIService::class.java) }
}

