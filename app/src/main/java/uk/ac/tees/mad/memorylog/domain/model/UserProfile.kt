package uk.ac.tees.mad.memorylog.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val email: String = "",
    val darkTheme: Boolean = false,
    val autoBackup: Boolean = true,
    val biometricEnabled: Boolean = false
)
