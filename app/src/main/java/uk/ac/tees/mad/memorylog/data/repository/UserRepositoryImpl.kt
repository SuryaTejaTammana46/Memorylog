package uk.ac.tees.mad.memorylog.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.domain.model.UserProfile
import uk.ac.tees.mad.memorylog.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    private val AUTO_BACKUP_KEY = booleanPreferencesKey("auto_backup")
    private val BIOMETRIC_KEY = booleanPreferencesKey("biometric_unlock")

    override suspend fun getUserProfile(): UserProfile? {
        val user = auth.currentUser ?: return null
        val doc = firestore.collection("users")
            .document(user.uid)
            .get()
            .await()

        return doc.toObject(UserProfile::class.java)
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        firestore.collection("users").document(profile.uid).set(profile).await()
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { it[DARK_THEME_KEY] = enabled }
    }

    override val darkThemeFlow = dataStore.data
        .map { prefs ->
            prefs[DARK_THEME_KEY] ?: false
        }


    override suspend fun setAutoBackup(enabled: Boolean) {
        dataStore.edit { it[AUTO_BACKUP_KEY] = enabled }
    }

    override suspend fun setBiometric(enabled: Boolean) {
        dataStore.edit { it[BIOMETRIC_KEY] = enabled }
    }

    override suspend fun logout() {
        auth.signOut()
        dataStore.edit { it.clear() }
    }

    override suspend fun clearCache() {
        // TODO: clear local Room DB and image cache
    }
}
