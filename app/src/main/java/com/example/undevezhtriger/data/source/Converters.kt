package com.example.undevezhtriger.data.source

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromListToString(list: List<Long>): String = Json.encodeToString(list)

    @TypeConverter
    fun fromStringToList(value: String): List<Long> = Json.decodeFromString(value)
}
