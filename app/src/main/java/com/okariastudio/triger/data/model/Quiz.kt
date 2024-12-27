package com.okariastudio.triger.data.model

data class Quiz(
    val words: List<Ger?> = emptyList(),
    val exactWord: Ger? = null,
    val score: Int = 0
)
