package uk.ac.tees.mad.memorylog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.local.entity.MemoryEntity

@Database(
    entities = [MemoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
}