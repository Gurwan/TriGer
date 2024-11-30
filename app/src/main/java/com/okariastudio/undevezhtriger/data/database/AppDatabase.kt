package com.okariastudio.undevezhtriger.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.okariastudio.undevezhtriger.data.dao.BrezhodexSectionDao
import com.okariastudio.undevezhtriger.data.dao.GerDao
import com.okariastudio.undevezhtriger.data.dao.QuizDao
import com.okariastudio.undevezhtriger.data.dao.SettingsDao
import com.okariastudio.undevezhtriger.data.dao.StatistiquesDao
import com.okariastudio.undevezhtriger.data.model.BrezhodexSection
import com.okariastudio.undevezhtriger.data.model.Ger
import com.okariastudio.undevezhtriger.data.model.Quiz
import com.okariastudio.undevezhtriger.data.model.Settings
import com.okariastudio.undevezhtriger.data.model.Statistiques
import com.okariastudio.undevezhtriger.data.source.Converters

@Database(
    entities = [Ger::class, BrezhodexSection::class, Quiz::class, Settings::class, Statistiques::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Si vous avez des types nécessitant une conversion
abstract class AppDatabase : RoomDatabase() {

    abstract fun gerDao(): GerDao
    abstract fun brezhodexSectionDao(): BrezhodexSectionDao
    abstract fun quizDao(): QuizDao
    abstract fun settingsDao(): SettingsDao
    abstract fun statistiquesDao(): StatistiquesDao

    companion object {
        const val DATABASE_NAME = "undevezhtriger.db"
    }
}

