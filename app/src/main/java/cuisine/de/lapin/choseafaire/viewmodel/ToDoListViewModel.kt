package cuisine.de.lapin.choseafaire.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cuisine.de.lapin.choseafaire.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoListViewModel(private val repository: ToDoRepository): ViewModel() {
    val todos = repository.allToDos

    fun insert(content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(content)
        }
    }
}

class ToDoListViewModelFactory(private val repository: ToDoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}