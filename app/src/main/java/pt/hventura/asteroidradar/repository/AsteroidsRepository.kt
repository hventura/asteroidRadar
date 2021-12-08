package pt.hventura.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pt.hventura.asteroidradar.database.DatabaseAsteroids
import pt.hventura.asteroidradar.models.Asteroid
import pt.hventura.asteroidradar.models.PictureOfDay
import pt.hventura.asteroidradar.models.asDatabaseModel
import pt.hventura.asteroidradar.models.asDomainModel
import pt.hventura.asteroidradar.network.NasaApi
import pt.hventura.asteroidradar.network.currentDateFormatted
import pt.hventura.asteroidradar.network.parseAsteroidsJsonResult

class AsteroidsRepository(private val database: DatabaseAsteroids) {

    val pictureOfDay: LiveData<PictureOfDay?> = Transformations.map(database.asteroidDao.getPictureOfDay()) {
        it?.asDomainModel()
    }

    val weekAsteroids: LiveData<List<Asteroid>?> = Transformations.map(database.asteroidDao.getWeekAsteroids()) {
        it?.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>?> = Transformations.map(database.asteroidDao.getTodayAsteroids()) {
        it?.asDomainModel()
    }

    val savedAsteroids: LiveData<List<Asteroid>?> = Transformations.map(database.asteroidDao.getSavedAsteroids()) {
        it?.asDomainModel()
    }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            val response = NasaApi.retrofitService.getImageOfDay()
            database.asteroidDao.insertPictureOfDay(response.asDatabaseModel())
        }
    }

    suspend fun removePictureOfDay(): Int {
        return withContext(Dispatchers.IO) {
            database.asteroidDao.removePicture()
        }
    }

    // JSONObject(response.string()) was the only way i found that worked!
    // But shoots warning "Inappropriate blocking method call" -> i understand why but did not found a way to work around this warning
    suspend fun getAsteroids() {
        withContext(Dispatchers.IO) {
            val response = NasaApi.retrofitService.getAsteroidsAsync().await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(response.string()))
            database.asteroidDao.insertAllAsteroids(*asteroids.asDatabaseModel())
        }
    }

    suspend fun removePastAsteroids(): Int {
        return withContext(Dispatchers.IO) {
            /**
             * currentDateFormatted() defined in network/NetworkUtils.kt
             * @returns Current day formatted as "yyyy-MM-dd"
             * */
            database.asteroidDao.removeAllAsteroids(currentDateFormatted())
        }
    }

}