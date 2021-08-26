package com.example.todo.data.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.model.ToDo
import com.example.todo.data.room.ToDoDao
import javax.sql.DataSource

class ToDoRepository(private val dataSource: ToDoDao) {
    suspend fun insertTodo(todo: ToDo) {
        dataSource.insertToDo(todo)
    }

    suspend fun deleteTodo(id: Int) {
        dataSource.deleteToDo(id)
    }

    fun getTodo(): LiveData<List<ToDo>> {
        return dataSource.getTodo()
    }
}