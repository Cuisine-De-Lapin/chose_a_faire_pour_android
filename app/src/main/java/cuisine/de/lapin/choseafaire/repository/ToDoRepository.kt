package cuisine.de.lapin.choseafaire.repository

import androidx.lifecycle.asLiveData
import cuisine.de.lapin.choseafaire.database.AppDataBase
import cuisine.de.lapin.choseafaire.database.ToDoListDao
import cuisine.de.lapin.choseafaire.model.ToDoListModel
import javax.inject.Inject

class ToDoRepository @Inject constructor(private val appDataBase: AppDataBase) {
    val allToDos = appDataBase.todoListDao().getAll().asLiveData()

    fun insert(content: String) {
        appDataBase.todoListDao().insert(ToDoListModel(0, content))
    }

    fun delete(toDoListModel: ToDoListModel) {
        appDataBase.todoListDao().delete(toDoListModel)
    }
}