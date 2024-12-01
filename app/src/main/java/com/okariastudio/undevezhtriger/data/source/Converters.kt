package com.okariastudio.undevezhtriger.data.source

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromListToString(list: List<Long>): String = Json.encodeToString(list)

    @TypeConverter
    fun fromStringToList(value: String): List<Long> = Json.decodeFromString(value)

    @TypeConverter
    fun fromMutableListToString(list: MutableList<Long>): String = Json.encodeToString(list)

    @TypeConverter
    fun fromStringtoMutableList(value: String): MutableList<Long> {
        val list: List<Long> = Json.decodeFromString(value)
        return list.toMutableList()
    }
}