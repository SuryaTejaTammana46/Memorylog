package uk.ac.tees.mad.memorylog.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
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
open class MemoryViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _memorySavedEvent = MutableStateFlow(false)
    val memorySavedEvent: StateFlow<Boolean> = _memorySavedEvent

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMemoryWithCheck(memory: Memory, onReplaceRequest: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val existsResult = repository.memoryExistsForDate(memory.date)
            if (existsResult.isSuccess && existsResult.getOrThrow()) {
                _uiState.value = UiState.Success(Unit)
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
            Log.d("MemoryVM", "Adding memory: $memory")
            val result = repository.addMemory(memory)
            Log.d("MemoryVM", "Add memory result: $result")
            _uiState.value = if (result.isSuccess) {
                UiState.Success(Unit)
            } else {
                UiState.Failure(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
            _memorySavedEvent.value = result.isSuccess
        }
    }

    fun resetEvent() {
        _memorySavedEvent.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hasMemoryToday(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            val exists = repository.hasMemoryFor(today)
            onResult(exists)
        }
    }


    //GALLERY

    // This will hold a single memory for the detail screen
    private val _selectedMemory = MutableStateFlow<Memory?>(null)
    val selectedMemory = _selectedMemory.asStateFlow()

    fun loadMemoryByDate(date: String) = viewModelScope.launch {
        val mem = repository.getMemoryForDate(date)
        _selectedMemory.value = mem
    }

    fun loadMemoryById(id: String) = viewModelScope.launch {
        val mem = repository.getMemoryById(id)
        _selectedMemory.value = mem
    }

    fun deleteMemoryById(id: String, onDone: (() -> Unit)? = null) = viewModelScope.launch {
        repository.deleteMemory(id)
        onDone?.invoke()
    }

    fun deleteMemory(date: String, onDone: (() -> Unit)? = null) = viewModelScope.launch {
        repository.deleteMemoryByDate(date)
        onDone?.invoke()
    }

    private var fullMemoryList: List<Memory> = emptyList()
    val allMemories = mutableStateOf<List<Memory>>(emptyList())
    val searchQuery = mutableStateOf("")
    val sortNewestFirst = mutableStateOf(true)

    fun loadGallery() = viewModelScope.launch {
        repository.getAllMemories().onSuccess { list ->
            fullMemoryList = list.sortedBy { it.date }
            applyCurrentFilters()
//            allMemories.value = list.sortedBy { it.date }
//            if (sortNewestFirst.value) allMemories.value = allMemories.value.reversed()
        }
    }

    private fun applyCurrentFilters() {
        val base = if (sortNewestFirst.value) {
            fullMemoryList.reversed()
        } else {
            fullMemoryList
        }

        val query = searchQuery.value
        allMemories.value = if (query.isBlank()) {
            base
        } else {
            base.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }
    }

    fun searchMemories(query: String) {
        searchQuery.value = query
        applyCurrentFilters()
    }

    fun toggleSort() {
        sortNewestFirst.value = !sortNewestFirst.value
        applyCurrentFilters()
    }


}