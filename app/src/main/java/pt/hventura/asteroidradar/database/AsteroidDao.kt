package pt.hventura.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.hventura.asteroidradar.models.AsteroidDatabase
import pt.hventura.asteroidradar.models.PictureOfDayDatabase
import pt.hventura.asteroidradar.network.currentDateFormatted
import pt.hventura.asteroidradar.network.currentWeekFormatted

@Dao
interface AsteroidDao {

    // ASTEROIDS

    /**
     * From Docs: A @Delete method can optionally return an int value indicating the number of rows that were deleted successfully.
     * https://developer.android.com/training/data-storage/room/accessing-data
     * */
    @Query("DELETE FROM asteroids WHERE closeApproachDate < :today")
    fun removeAllAsteroids(today: String): Int

    @Query("SELECT * FROM asteroids WHERE closeApproachDate IN (:weekDate) ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(weekDate: List<String> = currentWeekFormatted()): LiveData<List<AsteroidDatabase>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate = :startDate ORDER BY closeApproachDate ASC")
    fun getTodayAsteroids(startDate: String = currentDateFormatted()): LiveData<List<AsteroidDatabase>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getSavedAsteroids(): LiveData<List<AsteroidDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: AsteroidDatabase)


    // PICTURE OF DAY

    @Query("DELETE FROM picture_of_day")
    fun removePicture(): Int

    @Query("SELECT * FROM picture_of_day LIMIT 1")
    fun getPictureOfDay(): LiveData<PictureOfDayDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(pictureOfDay: PictureOfDayDatabase)

}