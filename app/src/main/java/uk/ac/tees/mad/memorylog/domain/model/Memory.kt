package uk.ac.tees.mad.memorylog.domain.model

data class Memory(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // ISO string for now
    val imagePath: String = "", // Local storage path (CameraX)
    val imageUrl: String = "" , // CLOUD URL (Firebase)
    val isSynced: Boolean = false,
    val userId: String,
)

fun Memory.toMap(imageUrlOverride: String? = null): Map<String, Any?> = mapOf(
    "id" to id,
    "title" to title,
    "description" to description,
    "date" to date,
    "imagePath" to imagePath,
    "imageUrl" to (imageUrlOverride ?: imageUrl),
    "isSynced" to isSynced
)
