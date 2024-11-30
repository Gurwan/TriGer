package com.okariastudio.undevezhtriger.data.repository

import com.okariastudio.undevezhtriger.data.dao.GerDao
import com.okariastudio.undevezhtriger.data.firebase.FirebaseService
import com.okariastudio.undevezhtriger.data.model.Ger

class GerRepository(private val gerDao: GerDao, private val firebaseService: FirebaseService) {
    suspend fun insertGer(ger: Ger) = gerDao.insert(ger)

    suspend fun getWordsForToday(): List<Ger> = gerDao.getWordsForToday()

    suspend fun markAsLearned(id: Long) = gerDao.markAsLearned(id)

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
