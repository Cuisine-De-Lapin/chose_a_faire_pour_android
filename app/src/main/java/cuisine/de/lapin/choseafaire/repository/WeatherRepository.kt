package cuisine.de.lapin.choseafaire.repository

import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import cuisine.de.lapin.choseafaire.network.WeatherService
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) {
    fun getWeather(): WeatherDataModel? {
        try {
            val result =
                weatherService.getWeatherData(37.2458731, 127.050937, NetworkProperties.API_KEY)
                    .execute()
            if (result.isSuccessful) {
                return result.body()
            } else {
                throw IOException("Wrong Result")
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}