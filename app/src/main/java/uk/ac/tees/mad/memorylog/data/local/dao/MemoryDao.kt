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

    @Query("SELECT * FROM memories WHERE userId = :uid ORDER BY date DESC ")
    fun getAllMemories(uid: String): Flow<List<MemoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(memories: List<MemoryEntity>)

    @Query("SELECT * FROM memories WHERE date = :date AND userId = :uid LIMIT 1")
    suspend fun getMemoryByDate(date: String, uid: String): MemoryEntity?

    @Query("DELETE FROM memories WHERE date = :date AND userId = :uid")
    suspend fun deleteByDate(date: String, uid: String)

    @Query("DELETE FROM memories WHERE id = :id AND userId = :uid")
    suspend fun deleteById(id: String, uid: String)

    @Query("SELECT * FROM memories WHERE isSynced = 0 AND userId = :uid")
    suspend fun getUnsyncedMemories(uid: String): List<MemoryEntity>

    @Query("UPDATE memories SET isSynced = 1 WHERE date = :date AND userId = :uid")
    suspend fun markAsSynced(date: String, uid: String)

    @Query("SELECT * FROM memories WHERE id = :id AND userId = :uid LIMIT 1")
    suspend fun getMemoryById(id: String, uid: String): MemoryEntity?

}