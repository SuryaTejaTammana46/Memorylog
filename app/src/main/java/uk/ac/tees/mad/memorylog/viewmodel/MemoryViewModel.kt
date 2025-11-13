package uk.ac.tees.mad.memorylog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import javax.inject.Inject

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val _memoryState = MutableStateFlow<UiState<List<Memory>>>(UiState.Idle)
    val memoryState: StateFlow<UiState<List<Memory>>> = _memoryState

    private val _addMemoryState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val addMemoryState: StateFlow<UiState<Unit>> = _addMemoryState

    fun loadAllMemories() {
        viewModelScope.launch {
            _memoryState.value = UiState.Loading
            memoryRepository.getAllMemories()
                .onSuccess { _memoryState.value = UiState.Success(it) }
                .onFailure { _memoryState.value = UiState.Failure(it) }
        }
    }

    fun addMemory(memory: Memory) {
        viewModelScope.launch {
            _addMemoryState.value = UiState.Loading
            memoryRepository.addMemory(memory)
                .onSuccess { _addMemoryState.value = UiState.Success(Unit) }
                .onFailure { _addMemoryState.value = UiState.Failure(it) }

            // refresh list after add
            loadAllMemories()
        }
    }

    fun deleteMemory(id: String) {
        viewModelScope.launch {
            memoryRepository.deleteMemory(id)
            loadAllMemories()
        }
    }
}