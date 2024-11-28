package com.example.undevezhtriger.data.dao

import androidx.room.*
import com.example.undevezhtriger.data.model.Settings

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
