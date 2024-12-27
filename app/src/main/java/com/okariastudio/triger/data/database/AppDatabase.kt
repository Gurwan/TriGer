package com.okariastudio.triger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.okariastudio.triger.data.dao.BrezhodexSectionDao
import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.dao.SettingsDao
import com.okariastudio.triger.data.dao.StatistiquesDao
import com.okariastudio.triger.data.model.BrezhodexSection
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.Settings
import com.okariastudio.triger.data.model.Statistiques
import com.okariastudio.triger.data.source.Converters

@Database(
    entities = [Ger::class, BrezhodexSection::class, Settings::class, Statistiques::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gerDao(): GerDao
    abstract fun brezhodexSectionDao(): BrezhodexSectionDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statistiquesDao(): StatistiquesDao

    companion object {
        const val DATABASE_NAME = "undevezhtriger.db"
    }
}

