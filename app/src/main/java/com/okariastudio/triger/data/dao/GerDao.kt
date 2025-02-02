package com.okariastudio.triger.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.okariastudio.triger.data.model.Ger

@Dao
interface GerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ger: Ger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gers: List<Ger>)

    @Query("SELECT id FROM ger")
    suspend fun getAllIds(): List<String>

    @Query("SELECT * FROM ger WHERE id = :id")
    suspend fun getById(id: String): Ger?

    @Query("SELECT * FROM ger WHERE id IN (:ids) AND isLearned = 0")
    suspend fun getGersByIds(ids: List<String>): List<Ger>

    @Query("SELECT * FROM ger WHERE id != :idGoodGer LIMIT 7")
    suspend fun getWrongGerForQuiz(idGoodGer: String): List<Ger>

    @Query("SELECT * FROM ger WHERE isLearned = 1")
    suspend fun getLearnedWords(): List<Ger>

    @Query("SELECT * FROM ger WHERE isLearned = 0 LIMIT 3")
    suspend fun getGerForToday(): List<Ger>

    @Query("UPDATE ger SET isLearned = 1, levelLearnings = levelLearnings + 1, lastLearningDate = CURRENT_TIMESTAMP WHERE id = :id")
    suspend fun markAsLearned(id: String)

    @Query("UPDATE ger SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("SELECT AVG(levelLearnings) FROM ger WHERE isLearned = 1")
    suspend fun getAverageLevel(): Double

    @Delete
    suspend fun delete(ger: Ger)
}