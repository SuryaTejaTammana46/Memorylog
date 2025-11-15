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

    private fun userMemoriesCollection() =
        firestore.collection("users")
            .document(auth.currentUser?.uid ?: "unknown_user")
            .collection("memories")

    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
        val collection = userMemoriesCollection()
        val docId = memory.id.ifBlank { collection.document().id }
        collection.document(docId).set(memory.copy(id = docId)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllMemories(): Result<List<Memory>> = try {
        val snapshot = userMemoriesCollection().get().await()
        Result.success(snapshot.toObjects(Memory::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMemoriesByDate(date: String): Result<List<Memory>> = try {
        val snapshot = userMemoriesCollection()
            .whereEqualTo("date", date)
            .get()
            .await()
        Result.success(snapshot.toObjects(Memory::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMemory(id: String): Result<Unit> = try {
        userMemoriesCollection().document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun memoryExistsForDate(date: String): Result<Boolean> = try {
        val snapshot = userMemoriesCollection()
            .whereEqualTo("date", date)
            .limit(1)
            .get()
            .await()
        Result.success(!snapshot.isEmpty)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMemoryByDate(date: String): Result<Unit> = try {
        val snapshot = userMemoriesCollection()
            .whereEqualTo("date", date)
            .get()
            .await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
