package com.example.undevezhtriger.data.model

//Word
data class Ger (
    val id: Long,
    val french: String = "",
    val breton: String = "",
    val description: String = "",
    val example: String = "",
    val isLearned: Boolean = false,
    val isFavorite: Boolean = false,
    val lastLearningDate: String = "",
    val levelLearnings: Int = 0
)
