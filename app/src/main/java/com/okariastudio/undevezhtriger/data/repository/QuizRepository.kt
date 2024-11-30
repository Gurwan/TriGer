package com.okariastudio.undevezhtriger.data.repository

import com.okariastudio.undevezhtriger.data.dao.QuizDao
import com.okariastudio.undevezhtriger.data.model.Quiz

class QuizRepository(private val quizDao: QuizDao) {
    suspend fun createQuiz(quiz: Quiz) = quizDao.insert(quiz)

    suspend fun getActiveQuiz(): Quiz? = quizDao.getActiveQuiz()

    suspend fun completeQuiz(id: String, score: Int) = quizDao.completeQuiz(id, score)
}
