package com.finalproject.smartwage.di

import android.content.Context
import androidx.room.Room
import com.finalproject.smartwage.data.local.SmartWageDatabase
import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Provide FirebaseAuth */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /** Provide AuthService */
    @Provides
    @Singleton
    fun provideAuthService(firebaseAuth: FirebaseAuth): AuthService =
        AuthService(firebaseAuth)

    /** Provide FirestoreService */
    @Provides
    @Singleton
    fun provideFirestoreService(): FirestoreService = FirestoreService()

    /** Provide Room Database */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartWageDatabase =
        Room.databaseBuilder(
            context,
            SmartWageDatabase::class.java,
            "smartwage_db"
        ).fallbackToDestructiveMigration()
            .build()

    /** Provide DAO Interfaces */
    @Provides fun provideUserDao(db: SmartWageDatabase): UserDao = db.userDao()

    /** Provide Repositories */
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, firestoreService: FirestoreService): UserRepository =
        UserRepository(userDao, firestoreService)
}