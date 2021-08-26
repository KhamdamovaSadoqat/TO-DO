package com.example.todo.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.room.ToDoDao
import java.lang.IllegalArgumentException

class TaskViewModelFactory(private val dataSource: ToDoDao): ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return TaskViewModel(dataSource) as T
        } else {
            throw IllegalArgumentException("TaskViewModel not found")
        }
    }
}