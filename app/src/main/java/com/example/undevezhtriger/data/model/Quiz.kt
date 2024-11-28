package com.example.undevezhtriger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class Quiz(
    @PrimaryKey val id: String = "",
    val words: List<Long> = emptyList(),
    val exactWord: Long? = null,
    val isCompleted: Boolean = false,
    val score: Int = 0
)
