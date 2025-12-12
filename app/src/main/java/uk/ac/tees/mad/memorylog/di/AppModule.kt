package uk.ac.tees.mad.memorylog.di

import android.content.ContentResolver
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.cloudinary.Cloudinary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.memorylog.data.local.AppDatabase
import uk.ac.tees.mad.memorylog.data.local.dao.MemoryDao
import uk.ac.tees.mad.memorylog.data.remote.CloudinaryUploader
import uk.ac.tees.mad.memorylog.data.repository.AuthRepositoryImpl
import uk.ac.tees.mad.memorylog.data.repository.MemoryRepositoryImpl
import uk.ac.tees.mad.memorylog.data.repository.UserRepositoryImpl
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository
import uk.ac.tees.mad.memorylog.domain.repository.UserRepository
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "settings_prefs"
    )

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

//    @Provides
//    @Singleton
//    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideMemoryRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        dao: MemoryDao,
        cloudUploader: CloudinaryUploader
    ): MemoryRepository =
        MemoryRepositoryImpl(firestore, firebaseAuth, dao, cloudUploader)

//    @Provides
//    @Singleton
//    fun providePreferencesDataStore(
//        @ApplicationContext context: Context
//    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        dataStore: DataStore<Preferences>
    ): UserRepository = UserRepositoryImpl(firestore, firebaseAuth, dataStore)


//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
//        return AppDatabase.getDatabase(context)
//    }

    @Provides
    @Singleton
    fun provideMemoryDao(db: AppDatabase): MemoryDao = db.memoryDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver

    @Provides
    @Singleton
    fun provideCloudinary(): Cloudinary =
        Cloudinary(
            mapOf(
                "cloud_name" to "dwzkplpan",
                "api_key" to "136394943347713",
                "api_secret" to "2xLIlaeMgx5dCkswA-UegBVPmQo"
            )
        )


}
