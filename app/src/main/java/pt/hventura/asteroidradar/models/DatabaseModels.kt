package pt.hventura.asteroidradar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Could i just re-use the data class Asteroid and
 * annotate it with @Entity and @Parcelize and use it for both
 * API and Database?
 */
@Entity(tableName = "asteroids")
data class AsteroidDatabase constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<AsteroidDatabase>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codeName = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

@Entity(tableName = "picture_of_day")
data class PictureOfDayDatabase constructor(
    @PrimaryKey
    val id: Long,
    val url: String,
    val mediaType: String,
    val title: String
)

fun PictureOfDayDatabase.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        url = url,
        mediaType = mediaType,
        title = title
    )
}