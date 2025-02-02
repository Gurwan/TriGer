package com.okariastudio.triger.data.repository

import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.model.Ger

class GerRepository(private val gerDao: GerDao, private val firebaseService: FirebaseService) {

    suspend fun getGerForToday(): List<Ger> = gerDao.getGerForToday()

    suspend fun getGersByIds(ids: List<String>) = gerDao.getGersByIds(ids)

    suspend fun getWrongGerForQuiz(idGoodGer: String): List<Ger> = gerDao.getWrongGerForQuiz(idGoodGer)

    suspend fun getGeriouInBrezhodex(): List<Ger> = gerDao.getLearnedWords()

    suspend fun getGerById(id: String): Ger? = gerDao.getById(id)

    suspend fun markAsLearned(id: String) = gerDao.markAsLearned(id)

    suspend fun getLearnedWords(): List<Ger> = gerDao.getLearnedWords()

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
