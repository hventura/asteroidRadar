package pt.hventura.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import pt.hventura.asteroidradar.models.AsteroidDatabase
import pt.hventura.asteroidradar.models.PictureOfDayDatabase

@Database(entities = [AsteroidDatabase::class, PictureOfDayDatabase::class], version = 1, exportSchema = false)
abstract class DatabaseAsteroids : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: DatabaseAsteroids

fun getDatabase(context: Context): DatabaseAsteroids {

    synchronized(Database::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = databaseBuilder(
                context.applicationContext,
                DatabaseAsteroids::class.java,
                "asteroids"
            ).build()
        }
    }

    return INSTANCE
}