package com.okariastudio.triger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.source.Converters

@Database(
    entities = [Ger::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gerDao(): GerDao

    companion object {
        const val DATABASE_NAME = "triger.db"
    }
}

