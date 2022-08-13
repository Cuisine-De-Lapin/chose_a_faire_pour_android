package cuisine.de.lapin.choseafaire.network

import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherService {
    @Headers("Content-Type: application/json")
    @GET("weather?lang=fr&units=metric")
    fun getWeatherData(@Query("lat")latitude: Double, @Query("lon")longitude: Double, @Query("appId")appId: String): Call<WeatherDataModel>
}