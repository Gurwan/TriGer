package com.example.undevezhtriger.data.dao

import androidx.room.*
import com.example.undevezhtriger.data.model.BrezhodexSection

@Dao
interface BrezhodexSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(section: BrezhodexSection)

    @Query("SELECT * FROM brezhodex_section")
    suspend fun getAllSections(): List<BrezhodexSection>

    @Query("SELECT * FROM brezhodex_section WHERE id = :id")
    suspend fun getSectionById(id: Long): BrezhodexSection?

    @Query("UPDATE brezhodex_section SET listGer = :listGer WHERE id = :id")
    suspend fun updateSectionWords(id: Long, listGer: List<Long>)
}
