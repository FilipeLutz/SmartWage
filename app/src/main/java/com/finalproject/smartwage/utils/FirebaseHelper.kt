package com.finalproject.smartwage.utils

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {
    fun getFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}