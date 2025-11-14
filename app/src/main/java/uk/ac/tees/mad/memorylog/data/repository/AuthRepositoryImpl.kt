package uk.ac.tees.mad.memorylog.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.domain.model.User
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> = try {
        Log.d("AuthRepo", "Attempting login for $email")
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.user
        if (user != null) {
            Log.d("AuthRepo", "Firebase current user: ${firebaseAuth.currentUser?.email}")
            Result.success(User(email = user.email ?: ""))
        } else {
            Result.failure(Exception("Login failed"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(email: String, password: String): Result<User> = try {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user
        if (user != null) {
            Result.success(User(email = user.email ?: ""))
        } else {
            Result.failure(Exception("Sign up failed"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun currentUser(): User? {
        val user = firebaseAuth.currentUser
        return user?.let { User(email = it.email ?: "") }
    }

    override fun checkIfUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}