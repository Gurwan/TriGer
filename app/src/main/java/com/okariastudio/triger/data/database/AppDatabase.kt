package com.okariastudio.triger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.dao.SettingsDao
import com.okariastudio.triger.data.dao.StatistiquesDao
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.Settings
import com.okariastudio.triger.data.model.Statistiques
import com.okariastudio.triger.data.source.Converters

@Database(
    entities = [Ger::class, Settings::class, Statistiques::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gerDao(): GerDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statistiquesDao(): StatistiquesDao

    companion object {
        const val DATABASE_NAME = "triger.db"
    }
}

