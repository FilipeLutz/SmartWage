package com.finalproject.smartwage

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

@HiltAndroidApp
class SmartWageApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseFirestore.getInstance()
    }
}