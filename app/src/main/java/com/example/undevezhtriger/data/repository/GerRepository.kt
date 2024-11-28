package com.example.undevezhtriger.data.repository

import com.example.undevezhtriger.data.dao.GerDao
import com.example.undevezhtriger.data.model.Ger

class GerRepository(private val gerDao: GerDao) {
    suspend fun insertGer(ger: Ger) = gerDao.insert(ger)

    suspend fun getWordsForToday(): List<Ger> = gerDao.getWordsForToday()

    suspend fun markAsLearned(id: Long) = gerDao.markAsLearned(id)

    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) = gerDao.updateFavoriteStatus(id, isFavorite)

    suspend fun getLearnedWords(): List<Ger> = gerDao.getLearnedWords()
}
