package uk.ac.tees.mad.memorylog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.memorylog.data.local.entity.MemoryEntity

@Dao
interface MemoryDao {

    @Query("SELECT * FROM memories ORDER BY date DESC")
    fun getAllMemories(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE date = :date LIMIT 1")
    suspend fun getMemory(date: String): MemoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memories: List<MemoryEntity>)

    @Delete
    suspend fun deleteMemory(memory: MemoryEntity)

    @Query("SELECT * FROM memories WHERE date = :date LIMIT 1")
    suspend fun getMemoryByDate(date: String): MemoryEntity?

    @Query("DELETE FROM memories WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("SELECT * FROM memories WHERE isSynced = 0")
    suspend fun getUnsyncedMemories(): List<MemoryEntity>

    @Query("UPDATE memories SET isSynced = 1 WHERE date = :date")
    suspend fun markAsSynced(date: String)


}