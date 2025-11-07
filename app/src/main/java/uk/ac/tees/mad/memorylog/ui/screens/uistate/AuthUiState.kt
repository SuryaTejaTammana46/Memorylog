package uk.ac.tees.mad.memorylog.ui.screens.uistate

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val success: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
)