package com.example.undevezhtriger.data.model

//Word
data class Ger (
    val id: String = "",
    val french: String = "",
    val breton: String = "",
    val description: String = "",
    val example: String = "",
    var isLearned: Boolean = false,
    var isFavorite: Boolean = false,
    var lastLearningDate: String = "",
    var levelLearnings: Int = 0
)
