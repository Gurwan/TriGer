package com.okariastudio.undevezhtriger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.undevezhtriger.data.model.Ger
import com.okariastudio.undevezhtriger.data.repository.GerRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val gerRepository: GerRepository
) : ViewModel() {

    private val _gersToday = MutableLiveData<List<Ger>>()
    val gersToday: LiveData<List<Ger>> = _gersToday

    private val _wrongGeriou = MutableLiveData<List<String>>()
    val wrongGeriou: LiveData<List<String>> = _wrongGeriou

    private val _gersBrezhodex = MutableLiveData<List<Ger>>()
    val gersBrezhodex: LiveData<List<Ger>> = _gersBrezhodex

    fun fetchGersForToday() {
        viewModelScope.launch {
            val gers = gerRepository.getGerForToday()
            _gersToday.postValue(gers)
        }
    }

    fun fetchWrongGersForQuiz(idGoodGer: String) {
        viewModelScope.launch {
            val geriou = gerRepository.getWrongGerForQuiz(idGoodGer)
            val ids = geriou.map { it.id }
            _wrongGeriou.postValue(ids)
        }
    }

    fun fetchGersInBrezhodex() {
        viewModelScope.launch {
            val geriou = gerRepository.getGeriouInBrezhodex()
            _gersBrezhodex.postValue(geriou)
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
