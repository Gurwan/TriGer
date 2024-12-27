package com.okariastudio.triger.data.dao

import androidx.room.*
import com.okariastudio.triger.data.model.Settings

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): Settings?

    @Query("UPDATE settings SET enableNotifications = :enabled WHERE id = 1")
    suspend fun updateNotifications(enabled: Boolean)

    @Query("UPDATE settings SET darkMode = :darkMode WHERE id = 1")
    suspend fun updateDarkMode(darkMode: Boolean)
}
