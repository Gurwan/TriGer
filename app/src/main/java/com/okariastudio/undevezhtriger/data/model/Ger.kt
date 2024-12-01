package com.okariastudio.undevezhtriger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ger")
data class Ger (
    @PrimaryKey val id: String = "",
    val french: String = "",
    val breton: String = "",
    val description: String = "",
    val example: String = "",
    val isLearned: Boolean = false,
    val isFavorite: Boolean = false,
    val lastLearningDate: String = "",
    val levelLearnings: Int = 0
)
