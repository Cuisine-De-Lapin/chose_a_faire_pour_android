package cuisine.de.lapin.choseafaire.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cuisine.de.lapin.choseafaire.model.ToDoListModel

@Database(entities = [ToDoListModel::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun todoListDao(): ToDoListDao

    companion object {
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}