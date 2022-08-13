package cuisine.de.lapin.choseafaire.repository

import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import cuisine.de.lapin.choseafaire.network.NetworkProperties
import java.io.IOException

class WeatherRepository {
    fun getWeather(): WeatherDataModel? {
        try {
            val result = NetworkProperties.getWeatherService()?.getWeatherData(37.2458731, 127.050937, NetworkProperties.API_KEY)?.execute()
            if (result?.isSuccessful == true) {
                return result.body()
            } else {
                throw IOException("Wrong Result")
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}