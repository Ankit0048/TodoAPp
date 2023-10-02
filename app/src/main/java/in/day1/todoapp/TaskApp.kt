package `in`.day1.todoapp

import android.app.Application

class TaskApp:Application() {
    val db by lazy{
        TaskDatabase.getInstance(this)
    }
}