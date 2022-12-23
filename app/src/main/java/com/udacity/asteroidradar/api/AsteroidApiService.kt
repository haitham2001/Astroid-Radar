package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {
    @GET(Constants.NASA_NEO_WS) //Get asteroids from nasa server based on the down parameters
    suspend fun getAsteroids(
        @Query("start_date")startDate: String,
        @Query("end_date")endDate: String,
        @Query("api_key")apiKey: String
    ): String

    @GET(Constants.PICTURE_OF_DAY_URI) //Get the picture of day from the server
    suspend fun getPictureOfDay(
        @Query("api_key")apiKey: String
    ): PictureOfDay
}

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

object AsteroidApi{
    private val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }

    suspend fun getWeekAsteroids(startDate: String,endDate: String): List<Asteroid>{
        val serverData = retrofitService.getAsteroids(startDate,endDate,Constants.API_KEY)
        return parseAsteroidsJsonResult(JSONObject(serverData)) // Returns all asteroid we got from the server
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return retrofitService.getPictureOfDay(Constants.API_KEY) // Returns The current Picture of day from the server
    }
}