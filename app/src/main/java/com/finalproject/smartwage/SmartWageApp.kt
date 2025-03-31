package com.finalproject.smartwage

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.request.CachePolicy
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp

/**
 * SmartWageApp is the main application class for the SmartWage app.
 * It initializes the image loader and Firebase services.
 *
 * This class is annotated with @HiltAndroidApp to enable dependency injection using Hilt.
 * It extends the Application class to provide a global application context.
 *
 * @HiltAndroidApp annotation is used to trigger Hilt's code generation.
 */

@HiltAndroidApp
class SmartWageApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Coil image loader with custom configuration
        val imageLoader = ImageLoader.Builder(this)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()

        // Set the custom image loader for Coil
        Coil.setImageLoader(imageLoader)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Initialize Firestore
        FirebaseFirestore.getInstance()
    }
}