package com.okariastudio.undevezhtriger.data.repository

import com.okariastudio.undevezhtriger.data.dao.StatistiquesDao
import com.okariastudio.undevezhtriger.data.model.Statistiques

class StatistiquesRepository(private val statistiquesDao: StatistiquesDao) {
    suspend fun getStatistics(): Statistiques? = statistiquesDao.getStatistics()

    suspend fun incrementWordsLearned() = statistiquesDao.incrementWordsLearned()

    suspend fun incrementDevezh() = statistiquesDao.incrementDevezh()

    suspend fun updateLongestStrike(strike: Int) = statistiquesDao.updateLongestStrike(strike)
}
