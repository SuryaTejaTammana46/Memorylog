package uk.ac.tees.mad.memorylog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.local.entity.MemoryEntity

@Database(
    entities = [MemoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "memorylog_db"
                )
                    // Use destructive migration for dev/testing
                    .fallbackToDestructiveMigration()

                    // Uncomment below and add migrations for production-safe upgrades
                    // .addMigrations(MIGRATION_1_2, MIGRATION_2_3, ...)

                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Example migration: 1 -> 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example: add a new column (update to match your schema changes)
                // database.execSQL("ALTER TABLE MemoryEntity ADD COLUMN newColumn TEXT DEFAULT '' NOT NULL")
            }
        }
    }
}