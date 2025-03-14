package com.okariastudio.triger.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.triger.R
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.Quiz
import com.okariastudio.triger.data.model.QuizLimit
import com.okariastudio.triger.data.model.QuizSettings
import com.okariastudio.triger.data.model.QuizTarget
import com.okariastudio.triger.data.model.QuizType
import com.okariastudio.triger.data.repository.GerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val gerRepository: GerRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    private val _isMinMode = MutableStateFlow(true)
    val isMinMode: StateFlow<Boolean> get() = _isMinMode

    private val _gersToday = MutableLiveData<List<Ger>>()
    val gersToday: LiveData<List<Ger>> = _gersToday

    private val _gersBrezhodex = MutableLiveData<List<Ger>>()
    val gersBrezhodex: LiveData<List<Ger>> = _gersBrezhodex

    private val _gersBrezhodexDevezh = MutableLiveData<List<Ger>>()
    val gersBrezhodexDevezh: LiveData<List<Ger>> = _gersBrezhodexDevezh

    private val _currentQuizItem = MutableStateFlow<Quiz?>(null)
    val currentQuizItem: StateFlow<Quiz?> = _currentQuizItem

    private val _statistiques = MutableLiveData<List<Pair<String, Any>>>()
    val statistiques: LiveData<List<Pair<String, Any>>> = _statistiques

    private val _totalGeriouLearned = MutableLiveData<Int>()
    val totalGeriouLearned: LiveData<Int> = _totalGeriouLearned

    private val _quizSettings = MutableLiveData<QuizSettings?>()
    val currentQuizSettings: LiveData<QuizSettings?> = _quizSettings

    private val _quizGeriou = MutableLiveData<List<Ger>?>()
    val currentQuizGeriou: LiveData<List<Ger>?> = _quizGeriou


    init {
        viewModelScope.launch {
            _isDarkTheme.value = isDarkMode()
            _isMinMode.value = isMinimalMode()
        }
    }

    fun fetchTotalGeriouLearned() {
        viewModelScope.launch {
            val geriou = gerRepository.getLearnedWords(Int.MAX_VALUE, QuizTarget.ALL_WORDS)
            _totalGeriouLearned.postValue(geriou.size)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            setDarkMode(newTheme)
        }
    }

    fun toggleMinMode() {
        viewModelScope.launch {
            val newMinMode = !isMinimalMode()
            _isMinMode.value = newMinMode
            setMinimalMode(newMinMode)
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

    fun fetchGersForToday() {
        viewModelScope.launch {
            val todayIds = getTodayGeriouIds()
            val geriou: List<Ger> = if (todayIds.isNotEmpty()) {
                gerRepository.getGersByIds(todayIds)
            } else {
                val nevezGeriou = gerRepository.getGerForToday()
                saveGeriouIds(nevezGeriou.map { it.id })
                nevezGeriou
            }
            _gersToday.postValue(geriou)
        }
    }

    fun fetchWrongGersForQuiz(exactWordId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val wrongGers = gerRepository.getWrongGerForQuiz(exactWordId)
                val exactWord = gerRepository.getGerById(exactWordId)
                val quizItem = Quiz(
                    exactWord = exactWord,
                    score = 0,
                    words = wrongGers
                )
                _currentQuizItem.value = quizItem
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchGersInBrezhodex() {
        viewModelScope.launch {
            val geriou = gerRepository.getGeriouInBrezhodex()
            val todayIds = getTodayGeriouIds()
            val devezhGeriou = geriou.filter { it.id in todayIds }
            _gersBrezhodexDevezh.postValue(devezhGeriou)
            val brezhodexGeriou = geriou.filter { it.id !in todayIds }
            _gersBrezhodex.postValue(brezhodexGeriou)
        }
    }

    fun validateQuiz(quizItem: Quiz) {
        viewModelScope.launch {
            try {
                quizItem.exactWord?.let {
                    gerRepository.markAsLearned(it.id)
                    Log.d("Brezhodex", "Ger marked as learned: ${it.id}")
                }
            } catch (e: Exception) {
                Log.e("Brezhodex", "Error adding nevez ger to brezhodex: ${e.message}")
            }
        }
    }

    fun synchronizeData() {
        viewModelScope.launch {
            try {
                gerRepository.synchronizeGerFromFirebase()
                Log.d("Sync", "Data synchronized successfully")
            } catch (e: Exception) {
                Log.e("Sync", "Error synchronizing data: ${e.message}")
            }
        }
    }

    fun startQuiz(quizType: QuizType, quizLimit: QuizLimit, limitValue: Int, target: QuizTarget) {
        _quizSettings.value = QuizSettings(
            type = quizType,
            limit = quizLimit,
            limitValue = limitValue,
            target = target,
            score = 0,
        )

        viewModelScope.launch {
            val limit = if (quizLimit == QuizLimit.N_WORDS) limitValue else Int.MAX_VALUE
            _quizGeriou.value = gerRepository.getLearnedWords(limit, target)
            loopQuiz(true)
        }
    }

    fun loopQuiz(firstTime: Boolean = false): Boolean {
        val quizSettingsToUpdate: QuizSettings = _quizSettings.value!!
        if (firstTime) {
            quizSettingsToUpdate.score += 1
        }
        _quizSettings.value = quizSettingsToUpdate
        val listGer = _quizGeriou.value?.toMutableList()
        if (listGer.isNullOrEmpty()) {
            finishQuiz()
            return false
        }
        val randomGer = listGer.random()
        fetchWrongGersForQuiz(randomGer.id)
        listGer.remove(randomGer)
        _quizGeriou.value = listGer.toList()
        return true
    }

    fun finishQuiz() {
        _quizSettings.value = null
        _currentQuizItem.value = null
        _quizGeriou.value = null
    }

    private fun setDarkMode(isDark: Boolean) {
        preferences.edit().putBoolean("is_dark_mode", isDark).apply()
    }

    private fun isDarkMode(): Boolean {
        return preferences.getBoolean("is_dark_mode", true)
    }

    private fun setMinimalMode(isMinimal: Boolean) {
        preferences.edit().putBoolean("is_min_mode", isMinimal).apply()
    }

    private fun isMinimalMode(): Boolean {
        return preferences.getBoolean("is_min_mode", false)
    }

    private fun saveGeriouIds(ids: List<String>) {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        preferences.edit().apply {
            putString("fetchDate", currentDate)
            putStringSet("geriouIds", ids.toSet())
            apply()
        }
    }

    private fun getTodayGeriouIds(): List<String> {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val storedDate = preferences.getString("fetchDate", null)
        val ids = preferences.getStringSet("geriouIds", emptySet()) ?: emptySet()
        return if (storedDate == currentDate) {
            ids.map { it.toString() }
        } else {
            preferences.edit().clear().apply()
            emptyList()
        }
    }
}
