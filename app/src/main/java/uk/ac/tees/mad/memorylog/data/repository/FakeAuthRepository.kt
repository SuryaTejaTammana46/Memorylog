package uk.ac.tees.mad.memorylog.data.repository

import kotlinx.coroutines.delay
import uk.ac.tees.mad.memorylog.domain.model.User
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository

class FakeAuthRepository : AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        delay(800) // simulate network delay
        return if (email == "test@example.com" && password == "password") {
            val user = User(email, password, "")
            currentUser = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    override suspend fun signUp(email: String, password: String): Result<User> {
        delay(800)
        val user = User(email, password, "")
        currentUser = user
        return Result.success(user)
    }

    override suspend fun logout() {
        delay(300)
        currentUser = null
    }

    override suspend fun currentUser(): User? = currentUser

    override fun checkIfUserLoggedIn(): Boolean = currentUser != null
}
