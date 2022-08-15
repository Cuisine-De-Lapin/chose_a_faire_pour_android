package cuisine.de.lapin.choseafaire.repository

import androidx.lifecycle.asLiveData
import cuisine.de.lapin.choseafaire.database.ToDoListDao
import cuisine.de.lapin.choseafaire.model.ToDoListModel

class ToDoRepository(private val toDoListDao: ToDoListDao) {
    val allToDos = toDoListDao.getAll().asLiveData()

    suspend fun insert(content: String) {
        toDoListDao.insert(ToDoListModel(0, content))
    }
}