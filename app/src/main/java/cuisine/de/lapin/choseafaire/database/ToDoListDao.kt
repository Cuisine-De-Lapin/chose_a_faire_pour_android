package cuisine.de.lapin.choseafaire.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cuisine.de.lapin.choseafaire.model.ToDoListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListDao {
    @Query("SELECT * FROM ToDoListModel")
    fun getAll(): Flow<List<ToDoListModel>>

    @Insert
    fun insert(toDoListModel: ToDoListModel)

    @Delete
    fun delete(toDoListModel: ToDoListModel)
}