package cuisine.de.lapin.choseafaire.database

import androidx.room.*
import cuisine.de.lapin.choseafaire.model.ToDoListModel
import kotlinx.coroutines.flow.Flow

@Database(entities = [ToDoListModel::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun todoListDao(): ToDoListDao
}

@Dao
interface ToDoListDao {
    @Query("SELECT * FROM ToDoListModel")
    fun getAll(): Flow<List<ToDoListModel>>

    @Insert
    fun insert(toDoListModel: ToDoListModel)

    @Delete
    fun delete(toDoListModel: ToDoListModel)
}