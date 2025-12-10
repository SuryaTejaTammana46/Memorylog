package uk.ac.tees.mad.memorylog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.AuthUiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun doLogin() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            authRepository.login(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(success = true, loading = false)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message, loading = false)
                }
        }
    }

    fun doSignUp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            authRepository.signUp(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(success = true, loading = false)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(error = it.message, loading = false)
                }
        }
    }

    fun doLogout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun checkIfUserLoggedIn() {
        val loggedIn = authRepository.checkIfUserLoggedIn()
        _uiState.value = _uiState.value.copy(success = loggedIn)
    }
}