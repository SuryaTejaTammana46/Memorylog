package uk.ac.tees.mad.memorylog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository
import uk.ac.tees.mad.memorylog.ui.screens.uistate.AuthUiState

class AuthViewModel(
    private val repository: AuthRepository = uk.ac.tees.mad.memorylog.data.repository.FakeAuthRepository()
): ViewModel() {
    //holds all fields and UI state

    init {
        checkIfUserLoggedIn()
    }

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        uiState = uiState.copy(confirmPassword = confirmPassword, error = null)
    }

    fun doSignUp() {
        if (uiState.email.isBlank() || uiState.password.isBlank() || uiState.confirmPassword.isBlank()) {
            uiState = uiState.copy(error = "All fields are required")
            return
        }
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(error = "Passwords do not match")
            return
        }

        uiState = uiState.copy(loading = true, error = null)
        viewModelScope.launch {
            repository.signUp(uiState.email, uiState.password)
                .onSuccess {
                    uiState = uiState.copy(loading = false, success = true)
                }
                .onFailure { e ->
                    uiState = uiState.copy(loading = false, error = e.message)
                }
        }
    }

    fun doLogin() {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(error = "Email and password are required")
            return
        }

        uiState = uiState.copy(loading = true, error = null)
        viewModelScope.launch {
            repository.login(uiState.email, uiState.password)
                .onSuccess {
                    uiState = uiState.copy(loading = false, success = true)
                }
                .onFailure { e ->
                    uiState = uiState.copy(loading = false, error = e.message)
                }
        }
    }

    fun doLogout() {
        viewModelScope.launch {
            repository.logout()
            uiState = AuthUiState()
        }
    }

    fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            val user = repository.currentUser()
            if (user != null) {
                uiState = uiState.copy(success = true)
            }
        }
    }
}