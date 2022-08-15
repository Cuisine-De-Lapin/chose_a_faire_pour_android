package cuisine.de.lapin.choseafaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cuisine.de.lapin.choseafaire.database.ToDoListDao
import cuisine.de.lapin.choseafaire.model.FlipDataModel
import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import cuisine.de.lapin.choseafaire.repository.MainRepository
import cuisine.de.lapin.choseafaire.repository.ToDoRepository
import cuisine.de.lapin.choseafaire.repository.WeatherRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel(): ViewModel() {
    private val _mainData = MutableLiveData<MainDataModel>()
    val mainData: LiveData<MainDataModel> = _mainData

    private val _weatherData = MutableLiveData<WeatherDataModel>()
    val weatherData: LiveData<WeatherDataModel> = _weatherData

    private val _filpData = MutableLiveData<List<FlipDataModel>>(ArrayList())
    val flipData: LiveData<List<FlipDataModel>> = _filpData

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    private val repository = MainRepository()
    private val weatherRepository = WeatherRepository()

    fun getMainData() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.getMainData()?.let {
                _mainData.postValue(it)
            }
        }
    }

    fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            weatherRepository.getWeather()?.let {
                _weatherData.postValue(it)
            }
        }
    }

    fun doFlip(isHead: Boolean) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val list = _filpData.value?.let {
                val newList = ArrayList<FlipDataModel>()
                newList.addAll(it)
                newList
            } ?: ArrayList()

            repository.getFlipData(isHead)?.let {
                list.add(it)
            }

            _filpData.postValue(list)
        }
    }

}