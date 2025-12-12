package uk.ac.tees.mad.memorylog.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CaptureMemoryViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMemory(
        title: String,
        description: String,
        photoPath: String
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val date = LocalDate.now().toString()
            val memory = Memory(
                id = "${uid}_${date}",
                title = title,
                description = description,
                date = date,
                imagePath = photoPath,
                imageUrl = "",
                isSynced = false,
                userId = uid
            )

            val result = memoryRepository.addMemory(memory)
            _uiState.value = result.fold(
                onSuccess = { UiState.Success(Unit) },
                onFailure = { UiState.Failure(it) }
            )
        }
    }
}
