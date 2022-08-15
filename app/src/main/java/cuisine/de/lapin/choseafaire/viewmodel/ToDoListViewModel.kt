package cuisine.de.lapin.choseafaire.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cuisine.de.lapin.choseafaire.model.ToDoListModel
import cuisine.de.lapin.choseafaire.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(private val repository: ToDoRepository) : ViewModel() {

}