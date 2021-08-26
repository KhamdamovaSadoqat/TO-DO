package com.example.todo.ui.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.room.ToDoDao
import java.lang.IllegalArgumentException
import kotlin.concurrent.thread

class AddViewModelFactory(private val dataSource: ToDoDao): ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)){
            return AddViewModel(dataSource) as T
        } else {
            throw IllegalArgumentException("AddViewModel not found")
        }
    }
}