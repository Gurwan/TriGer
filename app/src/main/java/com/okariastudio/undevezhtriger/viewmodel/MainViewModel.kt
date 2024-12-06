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

    fun fetchGersForToday() {
        viewModelScope.launch {
            val gers = gerRepository.getGerForToday()
            _gersToday.postValue(gers)
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
