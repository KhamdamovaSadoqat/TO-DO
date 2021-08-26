package com.example.todo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.data.model.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase : androidx.room.RoomDatabase() {
    abstract val dao: ToDoDao

    companion object {
        private var INSTANCE: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase {
            if (INSTANCE != null) {
                return INSTANCE!!
            }

            synchronized(this) {
                INSTANCE = Room.databaseBuilder(context, MyRoomDatabase::class.java, "TODO").build()
                return INSTANCE!!
            }
        }
    }
}