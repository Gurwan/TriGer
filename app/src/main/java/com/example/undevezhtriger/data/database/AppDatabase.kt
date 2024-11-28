package com.example.undevezhtriger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.undevezhtriger.data.dao.BrezhodexSectionDao
import com.example.undevezhtriger.data.dao.GerDao
import com.example.undevezhtriger.data.dao.QuizDao
import com.example.undevezhtriger.data.dao.SettingsDao
import com.example.undevezhtriger.data.dao.StatistiquesDao
import com.example.undevezhtriger.data.model.BrezhodexSection
import com.example.undevezhtriger.data.model.Ger
import com.example.undevezhtriger.data.model.Quiz
import com.example.undevezhtriger.data.model.Settings
import com.example.undevezhtriger.data.model.Statistiques
import com.example.undevezhtriger.data.source.Converters

@Database(
    entities = [Ger::class, BrezhodexSection::class, Quiz::class, Settings::class, Statistiques::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gerDao(): GerDao
    abstract fun brezhodexSectionDao(): BrezhodexSectionDao
    abstract fun quizDao(): QuizDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statistiquesDao(): StatistiquesDao
}