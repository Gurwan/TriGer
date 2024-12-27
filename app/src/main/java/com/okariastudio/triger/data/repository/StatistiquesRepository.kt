package com.okariastudio.triger.data.repository

import com.okariastudio.triger.data.dao.StatistiquesDao
import com.okariastudio.triger.data.model.Statistiques

class StatistiquesRepository(private val statistiquesDao: StatistiquesDao) {
    suspend fun getStatistics(): Statistiques? = statistiquesDao.getStatistics()

    suspend fun incrementWordsLearned() = statistiquesDao.incrementWordsLearned()

    suspend fun incrementDevezh() = statistiquesDao.incrementDevezh()

    suspend fun updateLongestStrike(strike: Int) = statistiquesDao.updateLongestStrike(strike)
}
