package com.okariastudio.triger.data.dao

import androidx.room.*
import com.okariastudio.triger.data.model.Statistiques

@Dao
interface StatistiquesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: Statistiques)

    @Query("SELECT * FROM statistiques WHERE id = 1")
    suspend fun getStatistics(): Statistiques?

    @Query("UPDATE statistiques SET totalWordsLearned = totalWordsLearned + 1 WHERE id = 1")
    suspend fun incrementWordsLearned()

    @Query("UPDATE statistiques SET totalDevezh = totalDevezh + 1, currentDevezhStrike = currentDevezhStrike + 1 WHERE id = 1")
    suspend fun incrementDevezh()

    @Query("UPDATE statistiques SET longestDevezhStrike = :strike WHERE id = 1 AND :strike > longestDevezhStrike")
    suspend fun updateLongestStrike(strike: Int)
}
