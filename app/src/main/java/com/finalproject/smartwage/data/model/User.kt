package com.finalproject.smartwage.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("phoneNumber") @set:PropertyName("phoneNumber") var phoneNumber: String = "",
    @get:PropertyName("taxCredit") @set:PropertyName("taxCredit") var taxCredit: Double = 4000.0,
    @get:PropertyName("profilePicture") @set:PropertyName("profilePicture") var profilePicture: String? = null
)