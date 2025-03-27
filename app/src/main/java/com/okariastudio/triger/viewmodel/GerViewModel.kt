package com.okariastudio.triger.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.triger.data.model.Ger
import com.okariastudio.triger.data.repository.GerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GerViewModel(
    private val gerRepository: GerRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _isMinMode = MutableStateFlow(true)
    val isMinMode: StateFlow<Boolean> get() = _isMinMode

    private val _gersToday = MutableLiveData<List<Ger>>()
    val gersToday: LiveData<List<Ger>> = _gersToday

    private val _gersBrezhodex = MutableLiveData<List<Ger>>()
    val gersBrezhodex: LiveData<List<Ger>> = _gersBrezhodex

    private val _gersBrezhodexDevezh = MutableLiveData<List<Ger>>()
    val gersBrezhodexDevezh: LiveData<List<Ger>> = _gersBrezhodexDevezh

    init {
        viewModelScope.launch {
            _isMinMode.value = isMinimalMode()
        }
    }

    fun toggleMinMode() {
        viewModelScope.launch {
            val newMinMode = !isMinimalMode()
            _isMinMode.value = newMinMode
            setMinimalMode(newMinMode)
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

    private fun setMinimalMode(isMinimal: Boolean) {
        preferences.edit().putBoolean("is_min_mode", isMinimal).apply()
    }

    private fun isMinimalMode(): Boolean {
        return preferences.getBoolean("is_min_mode", false)
    }
}