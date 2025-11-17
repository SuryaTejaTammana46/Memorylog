package uk.ac.tees.mad.memorylog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey val date: String, // yyyy-MM-dd (unique per day)
    val id: String,
    val title: String,
    val description: String,
    val imagePath: String // local URI (CameraX will insert here later)
)