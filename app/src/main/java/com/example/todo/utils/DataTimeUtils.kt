package com.example.todo.utils

class DataTimeUtils {

    fun timeToCompleteTime(time: String): String{
        val newTime = time.split(":").toMutableList()
        if (newTime[0].length == 1) newTime[0] = "0${newTime[0]}"
        if(newTime[1].length == 1) newTime[1] = "0${newTime[1]}"
        return "${newTime[0]}:${newTime[1]}"
    }

    fun dateToCompleteDate(date: String): String{
        val newDate = date.split(".").toMutableList()
        if(newDate[0].length == 1) newDate[0] = "0${newDate[0]}"
        if(newDate[1].length == 1) newDate[1] = "0${newDate[1]}"
        return "${newDate[0]}.${newDate[1]}.${newDate[2]}"
    }

}