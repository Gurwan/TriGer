package com.okariastudio.triger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.model.Quiz
import com.okariastudio.triger.data.repository.GerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val gerRepository: GerRepository
) : ViewModel() {

    private val _gersToday = MutableLiveData<List<Ger>>()
    val gersToday: LiveData<List<Ger>> = _gersToday

    private val _gersBrezhodex = MutableLiveData<List<Ger>>()
    val gersBrezhodex: LiveData<List<Ger>> = _gersBrezhodex

    private val _currentQuizItem = MutableStateFlow<Quiz?>(null)
    val currentQuizItem: StateFlow<Quiz?> = _currentQuizItem

    fun fetchGersForToday() {
        viewModelScope.launch {
            val gers = gerRepository.getGerForToday()
            _gersToday.postValue(gers)
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
            _gersBrezhodex.postValue(geriou)
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
}
