package uk.ac.tees.mad.memorylog.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {

    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(context.filesDir, "images").apply {
            if (!exists()) mkdirs()
        }
        return File(storageDir, "IMG_${timestamp}.jpg")
    }
}


fun initialOf(name: String): String {
    return name.trim().firstOrNull()?.uppercase()?.toString() ?: "?"
}
