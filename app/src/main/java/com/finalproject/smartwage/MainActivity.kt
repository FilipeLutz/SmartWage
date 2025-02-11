package com.finalproject.smartwage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.finalproject.smartwage.data.remote.FirebaseConfig
import com.finalproject.smartwage.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseConfig.initialize(this)

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}