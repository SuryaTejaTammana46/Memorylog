package uk.ac.tees.mad.memorylog.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.local.entity.MemoryEntity
import uk.ac.tees.mad.memorylog.domain.model.Memory
import javax.inject.Inject

class RoomMemoryRepository @Inject constructor(
    private val dao: MemoryDao
) {

    fun getAllMemories(): Flow<List<Memory>> =
        dao.getAllMemories().map { list -> list.map { it.toDomain() } }

    suspend fun getMemory(date: String): Memory? =
        dao.getMemory(date)?.toDomain()

    suspend fun insert(memory: Memory) =
        dao.insertMemory(memory.toEntity())

    suspend fun delete(memory: Memory) =
        dao.deleteMemory(memory.toEntity())
}

private fun MemoryEntity.toDomain() = Memory(title, description, date, imagePath)

private fun Memory.toEntity() = MemoryEntity(date, imagePath, title, description, System.currentTimeMillis())