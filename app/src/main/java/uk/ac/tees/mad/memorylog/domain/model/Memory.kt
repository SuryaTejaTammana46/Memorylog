package uk.ac.tees.mad.memorylog.domain.model

data class Memory(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "", // ISO string for now
    val imagePath: String = "", // Local storage path (CameraX)
)
