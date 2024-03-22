package com.example.pochcompose.database

import androidx.room.TypeConverter

class CustomConverter {
    @TypeConverter
    fun convertListToStrings(listOfString:List<String>):String{
        val stringBuilder = StringBuilder()
        listOfString.forEach {
            stringBuilder.append(it).append(",")
        }
        stringBuilder.setLength(stringBuilder.length-",".length)
        return stringBuilder.toString()
    }

    @TypeConverter
    fun  convertStringToList(string: String):List<String>{
         return string.split(",")
    }
}
