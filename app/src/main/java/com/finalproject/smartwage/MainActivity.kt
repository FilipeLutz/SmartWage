package com.finalproject.smartwage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.finalproject.smartwage.navigation.AppNavigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point for the application.
 * It initializes Firebase Authentication and sets up the navigation controller.
 *
 * @AndroidEntryPoint annotation is used for Hilt dependency injection.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Authentication
        val auth = FirebaseAuth.getInstance()
        // Check if the user is logged in
        val isUserLoggedIn = auth.currentUser != null
        // Initialize the navigation controller
        setContent {
            val navController = rememberNavController()
            AppNavigation(
                navController,
                isUserLoggedIn,
                userId = auth.currentUser?.uid,
            )
        }
    }
}