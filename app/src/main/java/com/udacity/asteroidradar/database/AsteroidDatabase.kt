package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {

    abstract val asteroidDao: AsteroidDatabaseDao

    companion object{

        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase{
            // To make sure it is synchronized across all threads
            synchronized(this){
                // Checks if we didn't have an instance (not initialized) of the database before or not
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_database"
                    )
                        .fallbackToDestructiveMigration().build()
                }

                return INSTANCE
            }
        }
    }
}
