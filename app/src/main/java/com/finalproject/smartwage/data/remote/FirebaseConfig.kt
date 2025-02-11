package com.finalproject.smartwage.data.remote

import android.annotation.SuppressLint
import com.finalproject.smartwage.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")

object FirebaseConfig {
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    fun initialize(application: MainActivity) {
        FirebaseApp.initializeApp(application)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }
}