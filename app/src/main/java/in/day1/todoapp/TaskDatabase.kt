package `in`.day1.todoapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase:RoomDatabase() {

    abstract fun taskDao():TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase?= null

        fun getInstance (context: Context): TaskDatabase {
            synchronized(this) {
                var instance = INSTANCE
// Create a database if no database is been created now
                if(instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        TaskDatabase::class.java,
                        "task_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}