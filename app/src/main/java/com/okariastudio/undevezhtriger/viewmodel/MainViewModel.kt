package com.okariastudio.undevezhtriger.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.okariastudio.undevezhtriger.data.repository.GerRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val gerRepository: GerRepository
) : ViewModel() {

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
