package com.okariastudio.triger.data.model

data class QuizSettings(
    val type: QuizType,
    val limit: QuizLimit,
    val limitValue: Int,
    val target: QuizTarget,
    var score: Int,
)
