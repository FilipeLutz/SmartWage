package com.finalproject.smartwage.di

import com.finalproject.smartwage.data.remote.AuthService
import com.finalproject.smartwage.data.remote.FirestoreService
import com.finalproject.smartwage.data.repository.AuthRepository
import com.finalproject.smartwage.data.repository.UserRepository
import com.finalproject.smartwage.data.repository.IncomeRepository
import com.finalproject.smartwage.data.repository.ExpenseRepository
import com.finalproject.smartwage.data.repository.TaxRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Makes it available app-wide
object AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService = AuthService()

    @Provides
    @Singleton
    fun provideFirestoreService(): FirestoreService = FirestoreService()

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository =
        AuthRepository(authService)

    @Provides
    @Singleton
    fun provideUserRepository(firestoreService: FirestoreService): UserRepository =
        UserRepository(firestoreService)

    @Provides
    @Singleton
    fun provideIncomeRepository(firestoreService: FirestoreService): IncomeRepository =
        IncomeRepository(firestoreService)

    @Provides
    @Singleton
    fun provideExpenseRepository(firestoreService: FirestoreService): ExpenseRepository =
        ExpenseRepository(firestoreService)

    @Provides
    @Singleton
    fun provideTaxRepository(firestoreService: FirestoreService): TaxRepository =
        TaxRepository(firestoreService)
}