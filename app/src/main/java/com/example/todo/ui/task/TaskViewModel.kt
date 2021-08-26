package com.example.todo.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.ToDoDao

class TaskViewModel(dataSource: ToDoDao) : ViewModel() {

    private val repo = ToDoRepository(dataSource)
    private var todo: LiveData<List<ToDo>>? = null

    init{
        todo = repo.getTodo()
    }

    fun getTodo(): LiveData<List<ToDo>>?{
        return todo
    }

}