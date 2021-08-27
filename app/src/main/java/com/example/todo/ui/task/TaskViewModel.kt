package com.example.todo.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.ToDoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(dataSource: ToDoDao) : ViewModel() {

    private val repo = ToDoRepository(dataSource)
    private var todo: LiveData<List<ToDo>>? = null

    init{
        todo = repo.getTodo()
    }

    fun getTodo(): LiveData<List<ToDo>>?{
        return todo
    }

//    fun deleteTodo(position: Int){
//        viewModelScope.launch{
//            withContext(Dispatchers.IO){
//                repo.deleteTodo(position)
//            }
//        }
//    }

}