package uk.ac.tees.mad.memorylog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import uk.ac.tees.mad.memorylog.domain.repository.UserRepository

@HiltViewModel
class ThemeViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> =
        userRepository.darkThemeFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )
}
