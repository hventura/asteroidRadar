package pt.hventura.asteroidradar.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import pt.hventura.asteroidradar.models.PictureOfDay
import pt.hventura.asteroidradar.utils.Constants.API_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getImageOfDay(
        @Query("api_key") key: String = API_TOKEN
    ): PictureOfDay

    @GET("neo/rest/v1/feed")
    fun getAsteroidsAsync(
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("api_key") key: String = API_TOKEN
    ): Deferred<ResponseBody>
}