package com.okariastudio.triger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class QuizViewModel(
    private val gerRepository: GerRepository,
) : ViewModel() {

    private val _currentQuizItem = MutableStateFlow<Quiz?>(null)
    val currentQuizItem: StateFlow<Quiz?> = _currentQuizItem

    private val _totalGeriouLearned = MutableLiveData<Int>()
    val totalGeriouLearned: LiveData<Int> = _totalGeriouLearned

    private val _quizSettings = MutableLiveData<QuizSettings?>()
    val currentQuizSettings: LiveData<QuizSettings?> = _quizSettings

    private val _quizGeriou = MutableLiveData<List<Ger>?>()

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

    fun fetchTotalGeriouLearned() {
        viewModelScope.launch {
            val geriou = gerRepository.getLearnedWords(Int.MAX_VALUE, QuizTarget.ALL_WORDS)
            _totalGeriouLearned.postValue(geriou.size)
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
            loopQuiz(quizLimit == QuizLimit.N_WORDS)
        }
    }

    fun startSingleQuiz() {
        _quizSettings.value = null
    }

    fun loopQuiz(launchLimitN: Boolean = false): Boolean {
        val quizSettingsToUpdate: QuizSettings = _quizSettings.value!!

        if (!launchLimitN) {
            quizSettingsToUpdate.score += 1
        }

        _quizSettings.value = quizSettingsToUpdate
        val listGer = _quizGeriou.value?.toMutableList()

        if (listGer.isNullOrEmpty()) {
            return false
        }

        val randomGer = listGer.random()
        fetchWrongGersForQuiz(randomGer.id)
        listGer.remove(randomGer)
        _quizGeriou.value = listGer.toList()
        return true
    }


}
