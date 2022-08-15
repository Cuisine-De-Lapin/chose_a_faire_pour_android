package cuisine.de.lapin.choseafaire.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoListModel(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "content") val content: String
)