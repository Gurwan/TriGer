package com.example.undevezhtriger.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.undevezhtriger.data.model.Ger

@Dao
interface GerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ger: Ger)

    @Query("SELECT * FROM ger WHERE id = :id")
    suspend fun getById(id: Long): Ger?

    @Query("SELECT * FROM ger WHERE isLearned = 1")
    suspend fun getLearnedWords(): List<Ger>

    @Query("SELECT * FROM ger WHERE isLearned = 0 LIMIT 3")
    suspend fun getWordsForToday(): List<Ger>

    @Query("UPDATE ger SET isLearned = 1 WHERE id = :id")
    suspend fun markAsLearned(id: Long)

    @Query("UPDATE ger SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Delete
    suspend fun delete(ger: Ger)
}