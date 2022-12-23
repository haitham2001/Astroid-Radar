package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(asteroidList: List<Asteroid>)

    @Query("SELECT * FROM ASTEROID_TABLE ORDER BY close_approach_date")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM ASTEROID_TABLE WHERE close_approach_date >= :startDate & close_approach_date <= :endDate ORDER BY close_approach_date")
    fun getThisWeekAsteroid(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM ASTEROID_TABLE WHERE close_approach_date = :thisDay")
    fun getThisDayAsteroids(thisDay: String): Flow<List<Asteroid>>
}