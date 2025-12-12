package uk.ac.tees.mad.memorylog.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.local.entity.toDomain
import uk.ac.tees.mad.memorylog.data.local.entity.toEntity
import uk.ac.tees.mad.memorylog.data.remote.CloudinaryUploader
import uk.ac.tees.mad.memorylog.domain.model.Memory
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import java.io.File
import javax.inject.Inject

class MemoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dao: MemoryDao,
    private val cloudinaryUploader: CloudinaryUploader
) : MemoryRepository {


    private fun currentUid(): String? = auth.currentUser?.uid

    private fun userMemoriesCollection(uid: String) =
        firestore.collection("users")
            .document(uid)
            .collection("memories")


    override suspend fun addMemory(memory: Memory): Result<Unit> = try {
        val uid = currentUid() ?: error("No logged-in user")

        dao.insertMemory(memory.toEntity().copy(userId = uid))

        var finalMemory = memory.copy(
            id = "${uid}_${memory.date}",
            userId = uid
        )

        if (memory.imagePath.isNotBlank()) {
            val imageUrl = cloudinaryUploader.uploadImage(memory.imagePath)

            finalMemory = finalMemory.copy(
                imageUrl = imageUrl,
                isSynced = true
            )

            dao.insertMemory(finalMemory.toEntity().copy(userId = uid))
        }

        userMemoriesCollection(uid)
            .document(finalMemory.id)
            .set(finalMemory)
            .await()

        Result.success(Unit)

    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllMemories(): Result<List<Memory>> {
        return try {
            val uid = currentUid() ?: return Result.success(emptyList())

            // 1) Try local
            val localList = dao.getAllMemories(uid)
                .first()
                .map { it.toDomain() }

            if (localList.isNotEmpty()) {
                return Result.success(localList)
            }

            val snapshot = userMemoriesCollection(uid).get().await()
            val list = snapshot.toObjects(Memory::class.java)

            dao.insertAll(
                list.map { it.toEntity().copy(userId = uid) }
            )

            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getMemoriesByDate(date: String): Result<List<Memory>> {
        return try {
            val uid = currentUid() ?: return Result.success(emptyList())

            val local = dao.getMemoryByDate(date, uid)?.toDomain()
            if (local != null) return Result.success(listOf(local))

            val snapshot = userMemoriesCollection(uid)
                .whereEqualTo("date", date)
                .get()
                .await()

            Result.success(snapshot.toObjects(Memory::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMemoryForDate(date: String): Memory? {
        val uid = currentUid() ?: return null
        return dao.getMemoryByDate(date, uid)?.toDomain()
    }

    override suspend fun hasMemoryFor(date: String): Boolean {
        val uid = currentUid() ?: return false

        val local = dao.getMemoryByDate(date, uid)
        if (local != null) return true

        val snapshot = userMemoriesCollection(uid)
            .whereEqualTo("date", date)
            .limit(1)
            .get()
            .await()

        return !snapshot.isEmpty
    }

    override suspend fun memoryExistsForDate(date: String): Result<Boolean> {
        return try {
            val uid = currentUid() ?: return Result.success(false)

            val local = dao.getMemoryByDate(date, uid)
            if (local != null) return Result.success(true)

            val snapshot = userMemoriesCollection(uid)
                .whereEqualTo("date", date)
                .limit(1)
                .get()
                .await()

            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteMemory(id: String): Result<Unit> = try {
        val uid = currentUid() ?: return Result.failure(
            IllegalStateException("No logged-in user")
        )

//        dao.deleteById(id, uid)

        val snapshot = userMemoriesCollection(uid)
            .whereEqualTo("date", id) // you are using date as id here
            .get()
            .await()

        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }

        dao.deleteById(id, uid)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMemoryByDate(date: String): Result<Unit> = try {
        val uid = currentUid() ?: return Result.failure(
            IllegalStateException("No logged-in user")
        )

        dao.deleteByDate(date, uid)

        val snapshot = userMemoriesCollection(uid)
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


    override suspend fun getUnsyncedMemories(): List<Memory> {
        val uid = currentUid() ?: return emptyList()
        return dao.getUnsyncedMemories(uid).map { it.toDomain() }
    }

    override suspend fun markAsSynced(date: String) {
        val uid = currentUid() ?: return
        dao.markAsSynced(date, uid)
    }

    override suspend fun getMemoryById(id: String): Memory? {
        val uid = currentUid() ?: return null
        return dao.getMemoryById(id, uid)?.toDomain()
    }

}
