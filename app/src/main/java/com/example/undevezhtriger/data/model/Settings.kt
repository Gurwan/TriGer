package com.example.undevezhtriger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey val id: Long = 1L,
    val enableNotifications: Boolean = true,
    val darkMode: Boolean = false
)