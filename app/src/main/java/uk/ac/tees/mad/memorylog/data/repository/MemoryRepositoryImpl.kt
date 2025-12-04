package uk.ac.tees.mad.memorylog.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.local.entity.toDomain
import uk.ac.tees.mad.memorylog.data.local.entity.toEntity
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import javax.inject.Inject

class MemoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: MemoryDao,
) : MemoryRepository {

    private fun userMemoriesCollection() =
        firestore.collection("users")
            .document(auth.currentUser?.uid ?: "unknown_user")
            .collection("memories")

//    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
//        dao.insertMemory(memory.toEntity())
//        val collection = userMemoriesCollection()
//        val docId = memory.id.ifBlank { collection.document().id }
//        collection.document(docId).set(memory.copy(id = docId)).await()
//        Result.success(Unit)
//    } catch (e: Exception) {
//        Result.failure(e)
//    }

    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
        // Save locally first
        dao.insertMemory(memory.toEntity())

        // Upload image and metadata to Firebase Storage/Firestore (if user logged in)
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("users/$uid/memories/${memory.date}.jpg")

            // convert local path to Uri and upload
            storageRef.putFile(android.net.Uri.parse(memory.imagePath)).await()

            val downloadUrl = storageRef.downloadUrl.await().toString()

            val finalMemory = memory.copy(
                imagePath = downloadUrl,
                id = memory.date // using date as id
            )

            userMemoriesCollection().document(finalMemory.id).set(finalMemory).await()
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    override suspend fun getAllMemories(): Result<List<Memory>> {
        return try {
            val localList = dao.getAllMemories().first().map { it.toDomain() }
            if (localList.isNotEmpty()) {
                return Result.success(localList)
            }

            // fallback to Firestore
            val snapshot = userMemoriesCollection().get().await()
            val list = snapshot.toObjects(Memory::class.java)

            // cache locally (convert)
            dao.insertAll(list.map { it.toEntity() })

            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getMemoriesByDate(date: String): Result<List<Memory>> {
        return try {
            val local = dao.getMemoryByDate(date)?.toDomain()
            if (local != null) return Result.success(listOf(local))

            val snapshot = userMemoriesCollection()
                .whereEqualTo("date", date)
                .get()
                .await()
            Result.success(snapshot.toObjects(Memory::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMemory(id: String): Result<Unit> = try {
        // delete from firestore and local db (id is date)
        val snapshot = userMemoriesCollection().whereEqualTo("date", id).get().await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
        dao.deleteByDate(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    override suspend fun memoryExistsForDate(date: String): Result<Boolean> {
        return try {
            // check local db
            val local = dao.getMemoryByDate(date)
            if (local != null) return Result.success(true)

            val snapshot = userMemoriesCollection()
                .whereEqualTo("date", date)
                .limit(1)
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteMemoryByDate(date: String): Result<Unit> = try {
        dao.deleteByDate(date)
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

    override suspend fun hasMemoryFor(date: String): Boolean {
        val local = dao.getMemoryByDate(date)
        if (local != null) return true
        val snapshot = userMemoriesCollection()
            .whereEqualTo("date", date)
            .limit(1)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    override suspend fun getMemoryForDate(date: String): Memory? {
        return dao.getMemoryByDate(date)?.toDomain()
    }

    override suspend fun getUnsyncedMemories(): List<Memory> {
        return dao.getUnsyncedMemories().map { it.toDomain() }
    }

    override suspend fun markAsSynced(date: String) {
        return dao.markAsSynced(date)
    }

}
