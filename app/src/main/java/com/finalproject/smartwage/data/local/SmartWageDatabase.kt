package com.finalproject.smartwage.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.local.dao.SettingsDao
import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.Expenses
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.local.entities.User
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.local.entities.Settings

@Database(
    entities = [User::class, Income::class, Expenses::class, Tax::class, Settings::class],
    version = 1,
    exportSchema = false
)
abstract class SmartWageDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun taxDao(): TaxDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: SmartWageDatabase? = null

        fun getInstance(context: Context): SmartWageDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SmartWageDatabase::class.java,
                    "smartwage_db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}