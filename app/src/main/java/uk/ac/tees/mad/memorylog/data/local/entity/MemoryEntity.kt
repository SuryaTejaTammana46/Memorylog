package uk.ac.tees.mad.memorylog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.memorylog.domain.model.Memory

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey val date: String, // yyyy-MM-dd (unique per day)
    val imagePath: String, // local URI (CameraX will insert here later)
    val title: String,
    val description: String,
    val timestamp: Long
)

fun MemoryEntity.toDomain() = Memory(
    id = date,
    title = title,
    description = description,
    date = date,
    imagePath = imagePath
)

fun Memory.toEntity() = MemoryEntity(
    date = date,
    title = title,
    description = description,
    imagePath = imagePath,
    timestamp = System.currentTimeMillis()
)