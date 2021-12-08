package pt.hventura.asteroidradar.backgroundWork

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pt.hventura.asteroidradar.database.getDatabase
import pt.hventura.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class DataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.getAsteroids()
            repository.removePastAsteroids()
            repository.removePictureOfDay()
            repository.getPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}