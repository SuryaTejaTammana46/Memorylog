package uk.ac.tees.mad.memorylog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.memorylog.domain.model.Memory

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val date: String,
    val imagePath: String,
    val imageUrl: String,
    val timestamp: Long,
    val isSynced: Boolean,
    val userId: String
)

fun MemoryEntity.toDomain() = Memory(
    id = id,
    title = title,
    description = description,
    date = date,
    imagePath = imagePath,
    imageUrl = imageUrl,
    isSynced = isSynced,
    userId = userId

)

fun Memory.toEntity() = MemoryEntity(
    id = "${userId}_${date}",
    title = title,
    description = description,
    date = date,
    imagePath = imagePath,
    imageUrl = imageUrl,
    timestamp = System.currentTimeMillis(),
    isSynced = isSynced,
    userId = userId
)