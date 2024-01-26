package com.example.beautifulmind.dbRecording

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Double> {
        val listType: Type = object : TypeToken<ArrayList<Double?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Double?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
