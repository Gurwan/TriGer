package com.okariastudio.undevezhtriger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey val id: String = "",
    val words: List<String?> = emptyList(),
    val exactWord: String? = null,
    val isCompleted: Boolean = false,
    val score: Int = 0
)
