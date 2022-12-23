package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.getCurrentDay
import com.udacity.asteroidradar.Constants.getLastDayOfTheWeek
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val repo = AsteroidRepository(database)

    //All Asteroid in the Recycler View
    private val _allAsteroids = MutableLiveData<List<Asteroid>>()
    val allAsteroids: LiveData<List<Asteroid>>
        get() = _allAsteroids

    //Our current Picture of Day
    private val _picOfDay = MutableLiveData<PictureOfDay>()
    val picOfDay: LiveData<PictureOfDay>
        get() = _picOfDay

    //Check if we are going to navigate or not
    private val _navigateToAsteroidDetail = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetail: LiveData<Asteroid>
        get() = _navigateToAsteroidDetail

    init {
        refreshAdapterList()
        getCurrentList()
        getImageOfDay()
    }

    private fun refreshAdapterList(){
        viewModelScope.launch {
            repo.refreshAsteroids()
        }
    }

    //Normal Screen get all Asteroids
    fun getCurrentList(){
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids().collect {
                _allAsteroids.value = it
            }
        }
    }

    //(Accessed from the menu) Set The Asteroids that are only today in the list
    fun getToday(){
        viewModelScope.launch {
            database.asteroidDao.getThisDayAsteroids(getCurrentDay()).collect {
                _allAsteroids.value = it
            }
        }
    }

    //(Accessed from the menu) Set The Asteroids that are from this week in the list
    fun getWeek(){
        viewModelScope.launch(){
            database.asteroidDao.getThisWeekAsteroid(getCurrentDay(), getLastDayOfTheWeek()).collect(){
                _allAsteroids.value = it
            }
        }
    }

    //Set the current Image of the day
    private fun getImageOfDay(){
        viewModelScope.launch {
            _picOfDay.value = AsteroidApi.getPictureOfDay()
        }
    }

    fun onAsteroidNavigate(asteroid: Asteroid){
        _navigateToAsteroidDetail.value = asteroid
    }

    fun doneAsteroidNavigate(){
        _navigateToAsteroidDetail.value = null
    }
}