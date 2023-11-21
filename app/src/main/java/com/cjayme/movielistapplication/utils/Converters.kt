package com.cjayme.movielistapplication.utils

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.cjayme.movielistapplication.data.Result
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromJson(value: String): List<Result> {
        val listType = object : TypeToken<List<Result>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Result>): String {
        return Gson().toJson(list)
    }
}