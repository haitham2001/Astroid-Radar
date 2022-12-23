package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.api.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            val asteroids = AsteroidApi.getWeekAsteroids("","")
            database.asteroidDao.insertAsteroid(asteroids)
        }
    }
}