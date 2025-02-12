package com.okariastudio.triger.data.model

data class Quiz(
    val words: List<Ger?> = emptyList(),
    val exactWord: Ger? = null,
    val score: Int = 0
)

enum class QuizType {
    WRITE,
    CHOICE,
    BOTH
}

enum class QuizLimit {
    N_WORDS,
    X_MINUTES,
    NO_LIMIT
}

enum class QuizTarget {
    ALL_WORDS,
    RECENT_WORDS,
    OLD_WORDS,
    LEVEL_LEARNING
}