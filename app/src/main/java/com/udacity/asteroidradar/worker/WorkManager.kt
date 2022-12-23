package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import retrofit2.HttpException

class AsteroidWorkManager(context: Context,params: WorkerParameters): CoroutineWorker(context,params) {

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroids()
            Result.success()
        }
        catch (e: HttpException) {
            Result.retry()
        }
    }
}