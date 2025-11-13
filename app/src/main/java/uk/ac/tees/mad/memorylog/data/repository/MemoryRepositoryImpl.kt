package uk.ac.tees.mad.memorylog.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import javax.inject.Inject

class MemoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MemoryRepository {

    private val memoryCollection = firestore.collection("memories")

    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
        memoryCollection.document(memory.id.ifBlank { memoryCollection.document().id })
            .set(memory)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllMemories(): Result<List<Memory>> = try {
        val snapshot = memoryCollection.get().await()
        val memories = snapshot.toObjects(Memory::class.java)
        Result.success(memories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMemoriesByDate(date: String): Result<List<Memory>> = try {
        val snapshot = memoryCollection.whereEqualTo("date", date).get().await()
        val memories = snapshot.toObjects(Memory::class.java)
        Result.success(memories)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMemory(id: String): Result<Unit> = try {
        memoryCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
