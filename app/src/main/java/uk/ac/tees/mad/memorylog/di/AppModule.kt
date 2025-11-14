package uk.ac.tees.mad.memorylog.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uk.ac.tees.mad.memorylog.data.repository.AuthRepositoryImpl
import uk.ac.tees.mad.memorylog.data.repository.MemoryRepositoryImpl
import uk.ac.tees.mad.memorylog.domain.repository.AuthRepository
import uk.ac.tees.mad.memorylog.domain.repository.MemoryRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

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

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideMemoryRepository(firestore: FirebaseFirestore, firebaseAuth: FirebaseAuth): MemoryRepository =
        MemoryRepositoryImpl(firestore, firebaseAuth)
}