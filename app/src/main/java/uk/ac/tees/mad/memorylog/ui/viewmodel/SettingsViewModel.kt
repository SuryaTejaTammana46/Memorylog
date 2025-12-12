package uk.ac.tees.mad.memorylog.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import uk.ac.tees.mad.memorylog.domain.model.UserProfile
import uk.ac.tees.mad.memorylog.domain.repository.UserRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import uk.ac.tees.mad.memorylog.utils.initialOf
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _userProfileState= MutableStateFlow< UiState<UserProfile?>>(UiState.Idle)
    var userProfile: StateFlow<UiState<UserProfile?>> =_userProfileState

    init {
        loadProfile()
    }

    private fun loadProfile() = viewModelScope.launch {
        _userProfileState.value = UiState.Loading
        try {
            val profile = userRepository.getUserProfile()
            _userProfileState.value = UiState.Success(profile)
        } catch (e: Exception) {
            _userProfileState.value = UiState.Failure(e)
        }
    }

    fun updateTheme(enabled: Boolean) = viewModelScope.launch {
        userRepository.setDarkTheme(enabled)
        val current = (_userProfileState.value as? UiState.Success)?.data ?: return@launch
        _userProfileState.value = UiState.Success(current.copy(darkTheme = enabled))
    }


    fun updateAutoBackup(enabled: Boolean) = viewModelScope.launch {
        userRepository.setAutoBackup(enabled)
        val current = (_userProfileState.value as? UiState.Success)?.data ?: return@launch
        _userProfileState.value = UiState.Success(current.copy(autoBackup = enabled))
    }

    fun updateProfile(name: String) = viewModelScope.launch {
        val current = (_userProfileState.value as? UiState.Success<UserProfile?>)?.data ?: return@launch
        val updated = current.copy(name = name, avatarUrl = initialOf(name))
        userRepository.updateUserProfile(updated)
        _userProfileState.value = UiState.Success(updated)
    }

    fun clearCache() = viewModelScope.launch {
        userRepository.clearCache()
    }

    fun logout() = viewModelScope.launch {
        userRepository.logout()
        _userProfileState.value = UiState.Success(null)
    }
}
