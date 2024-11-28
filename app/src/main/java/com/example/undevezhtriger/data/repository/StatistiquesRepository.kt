package com.example.undevezhtriger.data.repository

import com.example.undevezhtriger.data.dao.StatistiquesDao
import com.example.undevezhtriger.data.model.Statistiques

class StatistiquesRepository(private val statistiquesDao: StatistiquesDao) {
    suspend fun getStatistics(): Statistiques? = statistiquesDao.getStatistics()

    suspend fun incrementWordsLearned() = statistiquesDao.incrementWordsLearned()

    suspend fun incrementDevezh() = statistiquesDao.incrementDevezh()

    suspend fun updateLongestStrike(strike: Int) = statistiquesDao.updateLongestStrike(strike)
}
