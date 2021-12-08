package pt.hventura.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import pt.hventura.asteroidradar.database.getDatabase
import pt.hventura.asteroidradar.models.Asteroid
import pt.hventura.asteroidradar.models.PictureOfDay
import pt.hventura.asteroidradar.repository.AsteroidsRepository
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    var picOfDay: LiveData<PictureOfDay?> = asteroidsRepository.pictureOfDay
    var asteroids: LiveData<List<Asteroid>?> = asteroidsRepository.weekAsteroids

    fun getWeekAsteroids() {
        asteroids = asteroidsRepository.weekAsteroids
    }

    fun getTodayAsteroids() {
        asteroids = asteroidsRepository.todayAsteroids
    }

    fun getSavedAsteroids() {
        asteroids = asteroidsRepository.savedAsteroids
    }

    fun refreshImageOfDay() = viewModelScope.launch {
        asteroidsRepository.getPictureOfDay()
        picOfDay = asteroidsRepository.pictureOfDay
    }

    fun refreshAsteroidList() = viewModelScope.launch {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                asteroidsRepository.getPictureOfDay()
                asteroidsRepository.getAsteroids()
            }
        } catch (e: IOException) {
            Timber.e(e.message)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}

