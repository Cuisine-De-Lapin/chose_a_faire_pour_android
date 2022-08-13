package cuisine.de.lapin.choseafaire.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkProperties {
    val BASE_URL = "https://cuisine-de-lapin.github.io/chose_a_faire/"
    val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val API_KEY = "ca76b2c004359546488727983687c841"

    private var service: MainService? = null
    private var weatherService: WeatherService? = null

    fun getService(): MainService? {
        if (service != null) {
            return service
        }
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        service = retrofit.create(MainService::class.java)

        return service
    }

    fun getWeatherService(): WeatherService? {
        if (weatherService != null) {
            return weatherService
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherService::class.java)

        return weatherService
    }


}


