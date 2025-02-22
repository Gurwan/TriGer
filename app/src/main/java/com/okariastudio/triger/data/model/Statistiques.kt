package com.okariastudio.triger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistiques")
data class Statistiques(
    @PrimaryKey(autoGenerate = true) val id: Long = 1L,
    val totalWordsLearned: Int = 0,
    val totalDevezh: Int = 0,
    val currentDevezhStrike: Int = 0,
    val longestDevezhStrike: Int = 0
)
