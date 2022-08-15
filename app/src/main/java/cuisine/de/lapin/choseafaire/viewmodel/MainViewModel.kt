package cuisine.de.lapin.choseafaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cuisine.de.lapin.choseafaire.datastore.LogInDataStore
import cuisine.de.lapin.choseafaire.model.FlipDataModel
import cuisine.de.lapin.choseafaire.model.MainDataModel
import cuisine.de.lapin.choseafaire.model.ToDoListModel
import cuisine.de.lapin.choseafaire.model.WeatherDataModel
import cuisine.de.lapin.choseafaire.repository.MainRepository
import cuisine.de.lapin.choseafaire.repository.ToDoRepository
import cuisine.de.lapin.choseafaire.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val weatherRepository: WeatherRepository,
    private val logInDataStore: LogInDataStore,
    private val todoRepository: ToDoRepository
): ViewModel() {
    private val _mainData = MutableLiveData<MainDataModel>()
    val mainData: LiveData<MainDataModel> = _mainData

    private val _weatherData = MutableLiveData<WeatherDataModel>()
    val weatherData: LiveData<WeatherDataModel> = _weatherData

    private val _flipData = MutableLiveData<List<FlipDataModel>>(ArrayList())
    val flipData: LiveData<List<FlipDataModel>> = _flipData

    val todos = todoRepository.allToDos

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

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
            val list = _flipData.value?.let {
                val newList = ArrayList<FlipDataModel>()
                newList.addAll(it)
                newList
            } ?: ArrayList()

            repository.getFlipData(isHead)?.let {
                list.add(it)
            }

            _flipData.postValue(list)
        }
    }

    fun getLoginName() = logInDataStore.getLoginName

    fun saveLoginName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            logInDataStore.saveLoginName(name)
        }
    }

    fun insert(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insert(content)
        }
    }

    fun delete(toDoListModel: ToDoListModel) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.delete(toDoListModel)
        }
    }

}