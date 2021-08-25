package com.example.todo.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.data.model.ToDo

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(todo: ToDo)

    @Query("DELETE FROM Todo where id = :id")
    fun deleteToDo(id: Int)

    @Query("SELECT * FROM Todo")
    fun getTodo(): LiveData<List<ToDo>>
}