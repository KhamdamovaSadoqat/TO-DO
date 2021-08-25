package com.example.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo")
data class ToDo(

    var taskName: String,
    var discription: String,
    var category: String?,
    var date: String?,
    var time: String?,
    var important: Boolean,
    var reminde: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)