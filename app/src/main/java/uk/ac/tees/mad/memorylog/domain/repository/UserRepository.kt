package uk.ac.tees.mad.memorylog.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.memorylog.domain.model.UserProfile

interface UserRepository {
    val darkThemeFlow: Flow<Boolean>
    suspend fun getUserProfile(): UserProfile?
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun setDarkTheme(enabled: Boolean)
    suspend fun setAutoBackup(enabled: Boolean)
    suspend fun setBiometric(enabled: Boolean)
    suspend fun logout()
    suspend fun clearCache()
}