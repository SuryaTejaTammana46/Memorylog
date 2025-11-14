package uk.ac.tees.mad.memorylog.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import javax.inject.Inject

class MemoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MemoryRepository {

    private fun userMemoryCollection() = auth.currentUser?.let { user ->
        firestore.collection("users")
            .document(user.uid)
            .collection("memories")
    } ?: throw IllegalStateException("User not logged in")

    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
        val collection = userMemoryCollection()
        val docRef = if (memory.id.isBlank()) collection.document() else collection.document(memory.id)
        val memoryWithId = memory.copy(id = docRef.id)
        docRef.set(memoryWithId).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllMemories(): Result<List<Memory>> = try {
        val collection = userMemoryCollection()
        val snapshot = collection.get().await()
        val memories = snapshot.toObjects(Memory::class.java)
        Result.success(memories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMemoriesByDate(date: String): Result<List<Memory>> = try {
        val collection = userMemoryCollection()
        val snapshot = collection.whereEqualTo("date", date).get().await()
        val memories = snapshot.toObjects(Memory::class.java)
        Result.success(memories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMemory(id: String): Result<Unit> = try {
        val collection = userMemoryCollection()
        collection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun memoryExistsForDate(date: String): Boolean {
        val snapshot = userMemoryCollection().whereEqualTo("date", date).get().await()
        return !snapshot.isEmpty
    }

    override suspend fun deleteMemoryByDate(date: String): Result<Unit> = try {
        val collection = userMemoryCollection()
        val snapshot = collection.whereEqualTo("date", date).get().await()
        if (!snapshot.isEmpty) {
            collection.document(snapshot.documents.first().id).delete().await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
