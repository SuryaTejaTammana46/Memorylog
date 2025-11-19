package uk.ac.tees.mad.memorylog.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMemoryWithCheck(memory: Memory, onReplaceRequest: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val today = LocalDate.now().toString()

            val existsResult = repository.memoryExistsForDate(today)
            if (existsResult.isSuccess && existsResult.getOrThrow()) {
                _uiState.value = UiState.Idle
                onReplaceRequest() // Trigger dialog
            } else {
                addMemory(memory)
            }
        }
    }

    fun replaceMemory(memory: Memory) {
        viewModelScope.launch {
            repository.deleteMemoryByDate(memory.date)
            addMemory(memory)
        }
    }

    private fun addMemory(memory: Memory) {
        viewModelScope.launch {
            val result = repository.addMemory(memory)
            _uiState.value = if (result.isSuccess) {
                UiState.Success(Unit)
            } else {
                UiState.Failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hasMemoryToday(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val exists = repository.hasMemoryFor(today)
            onResult(exists)
        }
    }

}