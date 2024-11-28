package com.example.undevezhtriger.data.repository

import com.example.undevezhtriger.data.dao.BrezhodexSectionDao
import com.example.undevezhtriger.data.model.BrezhodexSection

class BrezhodexSectionRepository(private val sectionDao: BrezhodexSectionDao) {
    suspend fun insertSection(section: BrezhodexSection) = sectionDao.insert(section)

    suspend fun getAllSections(): List<BrezhodexSection> = sectionDao.getAllSections()

    suspend fun updateSectionWords(id: Long, listGer: List<Long>) = sectionDao.updateSectionWords(id, listGer)
}
