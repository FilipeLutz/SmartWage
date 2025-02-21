package com.finalproject.smartwage.di

import android.content.Context
import androidx.room.Room
import com.finalproject.smartwage.data.local.SmartWageDatabase
import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.repository.AuthRepository
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.TaxRepository
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

    /** Provide Firebase Authentication */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /** Provide Firestore Service */
    @Provides
    @Singleton
    fun provideFirestoreService(): FirestoreService = FirestoreService()

    /** Provide AuthService */
    @Provides
    @Singleton
    fun provideAuthService(firebaseAuth: FirebaseAuth): AuthService =
        AuthService(firebaseAuth)

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
    @Provides
    @Singleton
    fun provideUserDao(db: SmartWageDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideIncomeDao(db: SmartWageDatabase): IncomeDao = db.incomeDao()

    @Provides
    @Singleton
    fun provideExpenseDao(db: SmartWageDatabase): ExpenseDao = db.expenseDao()

    @Provides
    @Singleton
    fun provideTaxDao(db: SmartWageDatabase): TaxDao = db.taxDao()

    /** Provide Repositories */
    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService, firestoreService: FirestoreService, userDao: UserDao): AuthRepository =
        AuthRepository(authService, firestoreService, userDao, FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, firestoreService: FirestoreService): UserRepository =
        UserRepository(userDao, firestoreService)

    @Provides
    @Singleton
    fun provideIncomeRepository(incomeDao: IncomeDao, firestoreService: FirestoreService): IncomeRepository =
        IncomeRepository(incomeDao, firestoreService, FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao, firestoreService: FirestoreService): ExpenseRepository =
        ExpenseRepository(expenseDao, firestoreService)

    @Provides
    @Singleton
    fun provideTaxRepository(taxDao: TaxDao, firestoreService: FirestoreService): TaxRepository =
        TaxRepository(taxDao, firestoreService)
}