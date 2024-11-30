package com.okariastudio.undevezhtriger.data.dao

import androidx.room.*
import com.okariastudio.undevezhtriger.data.model.Quiz

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quiz: Quiz)

    @Query("SELECT * FROM quiz WHERE id = :id")
    suspend fun getById(id: String): Quiz?

    @Query("SELECT * FROM quiz WHERE isCompleted = 0 LIMIT 1")
    suspend fun getActiveQuiz(): Quiz?

    @Query("UPDATE quiz SET isCompleted = 1, score = :score WHERE id = :id")
    suspend fun completeQuiz(id: String, score: Int)
}
