package com.vs18.healthdiary.viewmodel

import androidx.lifecycle.*
import com.vs18.healthdiary.data.entity.HealthEntry
import com.vs18.healthdiary.repository.HealthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HealthViewModel : ViewModel() {
    private val repository = HealthRepository()

    val allEntries: StateFlow<List<HealthEntry>> = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insert(entry: HealthEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun update(entry: HealthEntry) = viewModelScope.launch {
        repository.update(entry)
    }

    fun delete(entry: HealthEntry) = viewModelScope.launch {
        repository.delete(entry)
    }

    fun getEntriesInRange(start: Long, end: Long) = repository.getEntriesInRange(start, end)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}