package com.example.undevezhtriger.data.model

data class Quiz (
    val id: String = "",
    val words: List<Long> = emptyList(),
    val exactWord: Long? = null,
    val isCompleted: Boolean = false,
    val score: Int = 0,
)
