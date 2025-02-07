package com.finalproject.smartwage.data.model

import com.google.firebase.firestore.PropertyName

data class Tax(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("income") @set:PropertyName("income") var income: Double = 0.0,
    @get:PropertyName("taxPaid") @set:PropertyName("taxPaid") var taxPaid: Double = 0.0,
    @get:PropertyName("taxYear") @set:PropertyName("taxYear") var taxYear: Int = 0
)