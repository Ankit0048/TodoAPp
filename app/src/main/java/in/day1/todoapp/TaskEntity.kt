package `in`.day1.todoapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TODO-table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val task: String="",
    @ColumnInfo(name = "Time")
    val time: String="")

