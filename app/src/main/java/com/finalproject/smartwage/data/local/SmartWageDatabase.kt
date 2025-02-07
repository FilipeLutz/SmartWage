package com.finalproject.smartwage.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finalproject.smartwage.data.local.dao.ExpenseDao
import com.finalproject.smartwage.data.local.dao.IncomeDao
import com.finalproject.smartwage.data.local.dao.TaxDao
import com.finalproject.smartwage.data.local.dao.UserDao
import com.finalproject.smartwage.data.local.entities.Expense
import com.finalproject.smartwage.data.local.entities.Income
import com.finalproject.smartwage.data.local.entities.Tax
import com.finalproject.smartwage.data.local.entities.User

@Database(
    entities = [User::class, Income::class, Expense::class, Tax::class],
    version = 1,
    exportSchema = false
)
abstract class SmartWageDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun taxDao(): TaxDao

    companion object {
        @Volatile
        private var INSTANCE: SmartWageDatabase? = null

        fun getDatabase(context: Context): SmartWageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartWageDatabase::class.java,
                    "smartwage_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}