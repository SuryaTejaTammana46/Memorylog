package uk.ac.tees.mad.memorylog.data.remote

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.text.get

class CloudinaryUploader @Inject constructor(
    private val cloudinary: Cloudinary,
    private val contentResolver: ContentResolver
) {

    suspend fun uploadImage(filePath: String): String = withContext(Dispatchers.IO) {
        try {
            val file = resolveToFile(filePath)

            val response = cloudinary.uploader().upload(
                file,
                ObjectUtils.asMap(
                    "folder", "memorylog/images",
                    "resource_type", "image"
                )
            )

            response["secure_url"]?.toString()
                ?: throw IllegalStateException("Cloudinary URL missing")

        } catch (e: Exception) {
            Log.e("CloudinaryUploader", "Upload failed", e)
            throw e
        }
    }

    private fun resolveToFile(path: String): File {
        return if (path.startsWith("content://")) {
            val uri = Uri.parse(path)
            val inputStream = contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open URI: $uri")

            val tempFile = File.createTempFile("upload_", ".jpg")
            FileOutputStream(tempFile).use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } else {
            File(path).also {
                require(it.exists()) { "File does not exist: $path" }
            }
        }
    }
}
