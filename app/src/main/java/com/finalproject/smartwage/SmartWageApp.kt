package com.finalproject.smartwage

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import dagger.hilt.android.HiltAndroidApp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

@HiltAndroidApp
class SmartWageApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val imageLoader = ImageLoader.Builder(this)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()

        Coil.setImageLoader(imageLoader)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseFirestore.getInstance()
    }
}