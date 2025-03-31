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
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.local.entities.Settings
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.local.entities.User

/**
 * SmartWageDatabase is the Room database for the SmartWage application.
 * It contains the entities and DAOs for managing user data, income, expenses, tax, and settings.
 *
 * This database is a singleton, meaning only one instance of it can exist at a time.
 * It uses the Room library to create and manage the database.
 *
 * @Database annotation specifies the entities, version, and export schema.
 * The entities are the data classes that represent the tables in the database.
 * The version is the database version number, and exportSchema indicates whether to export the schema.
 */

@Database(
    // Entities
    entities = [
        User::class,
        Income::class,
        Expenses::class,
        Tax::class,
        Settings::class
    ],
    version = 1,
    exportSchema = false
)
// Abstract class for SmartWage Database
abstract class SmartWageDatabase : RoomDatabase() {
    // Abstract methods for DAOs
    abstract fun userDao(): UserDao
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun taxDao(): TaxDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        // Database instance
        @Volatile
        private var INSTANCE: SmartWageDatabase? = null
        // Get instance of SmartWage Database
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