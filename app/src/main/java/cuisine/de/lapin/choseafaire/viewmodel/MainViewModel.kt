package cuisine.de.lapin.choseafaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import cuisine.de.lapin.choseafaire.repository.MainRepository
import cuisine.de.lapin.choseafaire.repository.WeatherRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel: ViewModel() {
    private val _mainData = MutableLiveData<MainDataModel>()
    val mainData: LiveData<MainDataModel> = _mainData

    private val _weatherData = MutableLiveData<WeatherDataModel>()
    val weatherData: LiveData<WeatherDataModel> = _weatherData

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    fun init() {
        val repository = MainRepository()
        val weatherRepository = WeatherRepository()
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.getMainData()?.let {
                _mainData.postValue(it)
            }

            weatherRepository.getWeather()?.let {
                _weatherData.postValue(it)
            }
        }
    }
}