package com.okariastudio.triger.data.repository

import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.QuizTarget

class GerRepository(private val gerDao: GerDao, private val firebaseService: FirebaseService) {

    suspend fun getGerForToday(): List<Ger> = gerDao.getGerForToday()

    suspend fun getGersByIds(ids: List<String>) = gerDao.getGersByIds(ids)

    suspend fun getWrongGerForQuiz(idGoodGer: String): List<Ger> =
        gerDao.getWrongGerForQuiz(idGoodGer)

    suspend fun getGeriouInBrezhodex(): List<Ger> = gerDao.getLearnedWords(Int.MAX_VALUE)

    suspend fun getGerById(id: String): Ger? = gerDao.getById(id)

    suspend fun markAsLearned(id: String) = gerDao.markAsLearned(id)

    suspend fun getLearnedWords(limit: Int, target: QuizTarget): List<Ger> {
        return when (target) {
            QuizTarget.ALL_WORDS -> gerDao.getLearnedWords(limit)
            QuizTarget.RECENT_WORDS -> gerDao.getLearnedWordsRecent(limit)
            QuizTarget.OLD_WORDS -> gerDao.getLearnedWordsOld(limit)
            QuizTarget.LEVEL_LEARNING -> gerDao.getLearnedWordsUnknown(limit)
        }
    }

    suspend fun getIdsGeriou(): List<String> = gerDao.getAllIds()

    suspend fun getAverageLevel(): Double = gerDao.getAverageLevel()

    suspend fun synchronizeGerFromFirebase() {
        val firebaseGers = firebaseService.fetchAllGeriou()
        val localGerIds = gerDao.getAllIds()

        if (firebaseGers.size == localGerIds.size) {
            return
        }

        val newGeriou = firebaseGers.filter { ger -> ger.id !in localGerIds }

        if (newGeriou.isNotEmpty()) {
            gerDao.insertAll(newGeriou)
        }
    }
}
