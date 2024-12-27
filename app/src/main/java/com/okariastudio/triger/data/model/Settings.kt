package com.okariastudio.triger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) val id: Long = 1L,
    val enableNotifications: Boolean = true,
    val darkMode: Boolean = false
)