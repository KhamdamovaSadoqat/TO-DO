package com.example.todo.utils

import android.util.Log
import java.util.*

class DataTimeUtils {

    private val mCalendar = Calendar.getInstance()

    fun timeToCompleteTime(time: String): String {
        val newTime = time.split(":").toMutableList()
        if (newTime[0].length == 1) newTime[0] = "0${newTime[0]}"
        if (newTime[1].length == 1) newTime[1] = "0${newTime[1]}"
        return "${newTime[0]}:${newTime[1]}"
    }

    fun dateToCompleteDate(date: String): String {
        val newDate = date.split(".").toMutableList()
//        val i = newDate[1].toInt() + 1
//        newDate[1] = "$i"
        if (newDate[0].length == 1) newDate[0] = "0${newDate[0]}"
        if (newDate[1].length == 1) newDate[1] = "0${newDate[1]}"
        return "${newDate[0]}.${newDate[1]}.${newDate[2]}"
    }

    fun timeToLong(date: String?, time: String?): Long {
        if (date.isNullOrEmpty() || time.isNullOrEmpty()) return 0

        val calendar = mCalendar
        val newDate = date.split(".")
        val newTime = time.split(":")

        calendar.set(
            newDate[2].toInt(),
            newDate[1].toInt(),
            newDate[0].toInt(),
            newTime[0].toInt(),
            newTime[1].toInt(),
            0
        )
        return calendar.timeInMillis
    }

    fun dateWithoutYear(date: String?): String?{
        if(date.isNullOrEmpty()) return null
        val newDate = date.split(".")
        return "${newDate[0]}.${newDate[1]}"
    }

}