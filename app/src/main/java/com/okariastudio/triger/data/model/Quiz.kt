package com.okariastudio.triger.data.model

data class Quiz(
    val words: List<Ger?> = emptyList(),
    val exactWord: Ger? = null,
    val score: Int = 0
)

enum class QuizType(val value: String) {
    WRITE("Ecriture"),
    CHOICE("Choix"),
    BOTH("Ecriture et choix")
}

enum class QuizLimit(val value: String) {
    N_WORDS("Nombre donné de mots"),
    NO_LIMIT("Sans limite")
}

enum class QuizTarget(val value: String) {
    ALL_WORDS("Tous les mots"),
    RECENT_WORDS("Les mots les plus récents"),
    OLD_WORDS("Les mots les plus anciens"),
    LEVEL_LEARNING("Niveau d'apprentissage")
}

data class QuizSettings(
    val type: QuizType,
    val limit: QuizLimit,
    val limitValue: Int,
    val target: QuizTarget,
    var score: Int,
)