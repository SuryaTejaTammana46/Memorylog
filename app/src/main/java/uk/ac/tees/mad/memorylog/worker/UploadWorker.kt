package uk.ac.tees.mad.memorylog.worker

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.memorylog.domain.model.toMap
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MemoryRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val unsynced = repository.getUnsyncedMemories()
            val storageRef = FirebaseStorage.getInstance().reference
            val firestore = FirebaseFirestore.getInstance()

            for (memory in unsynced) {
                val imageUri = Uri.parse(memory.imagePath)

                // Upload image to Firebase Storage
                val fileRef = storageRef.child("memories/${memory.date}.jpg")
                fileRef.putFile(imageUri).await()

                // Get download URL and update Firestore
                val downloadUrl = fileRef.downloadUrl.await()
                val memoryMap = memory.toMap(downloadUrl.toString())
//                memoryMap["imageUrl"] = downloadUrl.toString()

                firestore.collection("memories")
                    .document(memory.date)
                    .set(memoryMap)
                    .await()

                // Mark memory as synced in local DB
                repository.markAsSynced(memory.date)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

fun scheduleUploadWorker(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}