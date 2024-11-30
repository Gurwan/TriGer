package com.okariastudio.undevezhtriger.data.repository

import com.okariastudio.undevezhtriger.data.dao.SettingsDao
import com.okariastudio.undevezhtriger.data.model.Settings

class SettingsRepository(private val settingsDao: SettingsDao) {
    suspend fun getSettings(): Settings? = settingsDao.getSettings()

    suspend fun updateNotifications(enabled: Boolean) = settingsDao.updateNotifications(enabled)

    suspend fun updateDarkMode(darkMode: Boolean) = settingsDao.updateDarkMode(darkMode)
}
