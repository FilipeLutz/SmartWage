package com.finalproject.smartwage.di

import android.content.Context
import androidx.room.Room
import com.finalproject.smartwage.data.local.SmartWageDatabase
import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.local.dao.SettingsDao
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
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule is a Hilt module that provides dependencies for the application.
 * It includes the Firebase Auth, Firestore, Room database, and various repositories.
 * It is annotated with @Module and @InstallIn to indicate that it is a Hilt module
 * and to specify the component in which it should be installed.
 */

@Module
// This annotation tells Hilt that this is a module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides the Firebase Auth instance
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Provides the Firestore Service instance
    @Provides
    @Singleton
    fun provideFirestoreService(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // Provides the Auth Service instance
    @Provides
    @Singleton
    fun provideAuthService(firebaseAuth: FirebaseAuth): AuthService =
        AuthService(firebaseAuth)

    // Provides Room Database instance
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartWageDatabase =
        Room.databaseBuilder(
            context,
            SmartWageDatabase::class.java,
            "smartwage_db"
        ).fallbackToDestructiveMigration()
            .build()

    // Provides Dao instances
    @Provides
    @Singleton
    fun provideUserDao(db: SmartWageDatabase): UserDao {
        return db.userDao()
    }

    // Provides Income Dao
    @Provides
    @Singleton
    fun provideIncomeDao(db: SmartWageDatabase): IncomeDao = db.incomeDao()

    // Provides Expense Dao
    @Provides
    @Singleton
    fun provideExpenseDao(db: SmartWageDatabase): ExpenseDao = db.expenseDao()

    // Provides Tax Dao
    @Provides
    @Singleton
    fun provideTaxDao(db: SmartWageDatabase): TaxDao = db.taxDao()

    // Provides Settings Dao
    @Provides
    fun provideSettingsDao(db: SmartWageDatabase): SettingsDao = db.settingsDao()

    // Provides AuthRepository instance
    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: AuthService,
        firestoreService: FirestoreService,
        userDao: UserDao
    ): AuthRepository =
        AuthRepository(authService, firestoreService, userDao, FirebaseAuth.getInstance())

    // Provides UserRepository instance
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        firestoreService: FirestoreService
    ): UserRepository =
        UserRepository(userDao, firestoreService, FirebaseAuth.getInstance())

    // Provides IncomeRepository instance
    @Provides
    @Singleton
    fun provideIncomeRepository(
        incomeDao: IncomeDao,
        firestoreService: FirestoreService,
        context: Context
    ): IncomeRepository =
        IncomeRepository(incomeDao, firestoreService, FirebaseAuth.getInstance(), context)

    // Provides ExpenseRepository instance
    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        firestoreService: FirestoreService
    ): ExpenseRepository =
        ExpenseRepository(expenseDao, firestoreService, FirebaseAuth.getInstance())

    // Provides TaxRepository instance
    @Provides
    @Singleton
    fun provideTaxRepository(taxDao: TaxDao, firestoreService: FirestoreService): TaxRepository =
        TaxRepository(taxDao, firestoreService)

    // Provides Context instance
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context
}