package com.nstorm.audiostream

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _socketStatus = MutableLiveData<Boolean>(false)
    val socketStatus = _socketStatus


    fun setStatus(status: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _socketStatus.value = status
        }
    }

}