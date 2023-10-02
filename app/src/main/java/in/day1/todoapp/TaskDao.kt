package `in`.day1.todoapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM `TODO-table`" )
    fun fetchAllTask(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM `TODO-table`where id=:id")
    fun fetchTaskById(id: Int): Flow<TaskEntity>

}