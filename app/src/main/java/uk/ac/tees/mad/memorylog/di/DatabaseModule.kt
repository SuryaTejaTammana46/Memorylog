package uk.ac.tees.mad.memorylog.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uk.ac.tees.mad.memorylog.data.local.AppDatabase
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "memory_log_db"
        ).build()

    @Provides
    fun provideMemoryDao(db: AppDatabase): MemoryDao = db.memoryDao()
}