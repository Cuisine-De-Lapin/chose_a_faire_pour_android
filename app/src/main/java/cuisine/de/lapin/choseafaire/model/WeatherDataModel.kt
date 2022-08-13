package cuisine.de.lapin.choseafaire.model

data class WeatherDataModel(val weather: List<Weather>, val main: Main, val name: String)

data class Weather(val id: Int, val main: String, val description: String)
data class Main(val temp: Double, val feels_like: Double, val temp_min: Double, val temp_max: Double, val pressure: Int, val humidity: Int)
