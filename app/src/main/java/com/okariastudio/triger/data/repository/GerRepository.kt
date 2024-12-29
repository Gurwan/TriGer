package com.okariastudio.triger.data.repository

import com.okariastudio.triger.data.dao.GerDao
import com.okariastudio.triger.data.firebase.FirebaseService
import com.okariastudio.triger.data.model.Ger

class GerRepository(private val gerDao: GerDao, private val firebaseService: FirebaseService) {
    suspend fun insertGer(ger: Ger) = gerDao.insert(ger)

    suspend fun getGerForToday(): List<Ger> = gerDao.getGerForToday()

    suspend fun getGersByIds(ids: List<String>) = gerDao.getGersByIds(ids)

    suspend fun getWrongGerForQuiz(idGoodGer: String): List<Ger> = gerDao.getWrongGerForQuiz(idGoodGer)

    suspend fun getGeriouInBrezhodex(): List<Ger> = gerDao.getLearnedWords()

    suspend fun getGerById(id: String): Ger? = gerDao.getById(id)

    suspend fun markAsLearned(id: String) = gerDao.markAsLearned(id)

    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) =
        gerDao.updateFavoriteStatus(id, isFavorite)

    suspend fun getLearnedWords(): List<Ger> = gerDao.getLearnedWords()

    suspend fun synchronizeGerFromFirebase() {
        val firebaseGers = firebaseService.fetchAllGeriou()
        val localGerIds = gerDao.getAllIds()

        val newGeriou = firebaseGers.filter { ger -> ger.id !in localGerIds }

        if (newGeriou.isNotEmpty()) {
            gerDao.insertAll(newGeriou)
        }
    }
}
