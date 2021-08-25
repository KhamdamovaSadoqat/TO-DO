package com.example.todo.ui.add

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.ToDoDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddViewModel(dataSource: ToDoDao) : ViewModel() {
    private val repo = ToDoRepository(dataSource)

    fun insertToDo(todo: ToDo) {
        viewModelScope.launch {
            withContext(IO) {
                repo.insertTodo(todo)
            }
        }
    }
}