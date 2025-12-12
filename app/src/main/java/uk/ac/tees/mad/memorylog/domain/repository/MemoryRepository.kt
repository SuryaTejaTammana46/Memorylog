package uk.ac.tees.mad.memorylog.domain.repository

import uk.ac.tees.mad.memorylog.domain.model.Memory

interface MemoryRepository {
    suspend fun addMemory(memory: Memory): Result<Unit>
    suspend fun getAllMemories(): Result<List<Memory>>
    suspend fun getMemoriesByDate(date: String): Result<List<Memory>>
    suspend fun deleteMemory(id: String): Result<Unit>
    suspend fun memoryExistsForDate(date: String): Result<Boolean>
    suspend fun deleteMemoryByDate(date: String): Result<Unit>
    suspend fun hasMemoryFor(date: String): Boolean
    suspend fun getMemoryForDate(date: String): Memory?
    suspend fun getUnsyncedMemories(): List<Memory>
    suspend fun markAsSynced(date: String)
    suspend fun getMemoryById(id: String): Memory?
}