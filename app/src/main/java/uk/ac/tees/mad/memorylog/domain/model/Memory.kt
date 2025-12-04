package uk.ac.tees.mad.memorylog.domain.model

data class Memory(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // ISO string for now
    val imagePath: String = "", // Local storage path (CameraX)
    val imageUrl: String = "" , // CLOUD URL (Firebase)
    val isSynced: Boolean = false
)

fun Memory.toMap(toString: String): Map<String, Any?> = mapOf(
    "id" to id,
    "title" to title,
    "description" to description,
    "date" to date,
    "imagePath" to imagePath,
    "imageUrl" to imageUrl,
    "isSynced" to isSynced
)
