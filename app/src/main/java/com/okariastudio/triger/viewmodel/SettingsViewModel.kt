package com.okariastudio.triger.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.QuizTarget
import com.okariastudio.triger.data.repository.GerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val gerRepository: GerRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    private val _statistiques = MutableLiveData<List<Pair<String, Any>>>()
    val statistiques: LiveData<List<Pair<String, Any>>> = _statistiques

    init {
        viewModelScope.launch {
            _isDarkTheme.value = isDarkMode()
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            setDarkMode(newTheme)
        }
    }

    @SuppressLint("DefaultLocale")
    fun getStatistics(context: Context) {
        viewModelScope.launch {
            val gerLearnedNumber =
                gerRepository.getLearnedWords(Int.MAX_VALUE, QuizTarget.ALL_WORDS).size
            val gerNumber = gerRepository.getIdsGeriou().size
            val averageLevel = gerRepository.getAverageLevel()
            _statistiques.value = listOf(
                context.getString(R.string.stats_ger_learned) to gerLearnedNumber,
                context.getString(R.string.stats_ger_number) to gerNumber,
                context.getString(R.string.stats_average_level) to String.format(
                    "%.2f",
                    averageLevel
                ),
            )
        }
    }

    private fun setDarkMode(isDark: Boolean) {
        preferences.edit().putBoolean("is_dark_mode", isDark).apply()
    }

    private fun isDarkMode(): Boolean {
        return preferences.getBoolean("is_dark_mode", true)
    }
}
