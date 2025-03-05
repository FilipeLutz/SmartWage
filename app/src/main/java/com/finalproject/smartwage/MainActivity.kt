package com.finalproject.smartwage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.finalproject.smartwage.navigation.AppNavigation
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val isUserLoggedIn = auth.currentUser != null

        setContent {
            val navController = rememberNavController()
            AppNavigation(
                navController,
                isUserLoggedIn,
                userId = auth.currentUser?.uid
            )
        }
    }
}