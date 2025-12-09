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
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _userProfileState= MutableStateFlow< UiState<UserProfile?>>(UiState.Idle)
    var userProfile: StateFlow<UiState<UserProfile?>> =_userProfileState

    init {
        viewModelScope.launch {
            viewModelScope.launch {
                _userProfileState.value= UiState.Loading
                try {
                    var response = userRepository.getUserProfile()
                    _userProfileState.value= UiState.Success(response)
                }catch (e:Exception){
                    _userProfileState.value= UiState.Failure(e)
                }
            }

        }
    }

    fun updateTheme(enabled: Boolean) = viewModelScope.launch {
        userRepository.setDarkTheme(enabled)
    }

    fun updateAutoBackup(enabled: Boolean) = viewModelScope.launch {
        userRepository.setAutoBackup(enabled)
    }

    fun updateBiometric(enabled: Boolean) = viewModelScope.launch {
        userRepository.setBiometric(enabled)
    }

    fun updateProfile(name: String, avatarUrl: String) = viewModelScope.launch {
        userProfile?.let {
//            val updated = it.copy(name = name, avatarUrl = avatarUrl)
//            userRepository.updateUserProfile(updated)
//            userProfile = updated
        }
    }

    fun clearCache() = viewModelScope.launch {
        userRepository.clearCache()
    }

    fun logout() = viewModelScope.launch {
        userRepository.logout()
    }
}
