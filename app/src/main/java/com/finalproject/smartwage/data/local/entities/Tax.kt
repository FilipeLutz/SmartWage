package com.finalproject.smartwage.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import java.util.UUID

/**
 * Tax is a data class that represents the tax information of a user.
 * It includes various properties related to tax calculations.
 *
 * @property id The unique identifier for the tax record.
 * @property userId The unique identifier for the user.
 * @property totalTax The total tax amount.
 * @property totalIncome The total income amount.
 * @property totalPAYE The total Pay As You Earn (PAYE) tax.
 * @property totalUSC The total Universal Social Charge (USC).
 * @property totalPRSI The total Pay Related Social Insurance (PRSI).
 * @property taxCredit The tax credit amount.
 * @property tuitionFeeRelief The tuition fee relief amount.
 * @property rentTaxCredit The rent tax credit amount.
 * @property expectedPAYE The expected PAYE tax.
 * @property expectedUSC The expected USC.
 * @property expectedPRSI The expected PRSI.
 * @property expectedTotalTax The expected total tax amount.
 * @property currentWeek The current week number.
 * @property overpaidTax The amount of tax overpaid.
 * @property underpaidTax The amount of tax underpaid.
 * @property lastCalculated The timestamp of the last tax calculation.
 */

@Entity(tableName = "tax")
// Data class for Tax
data class Tax(
    // Primary key
    @PrimaryKey @get:PropertyName("id") @set:PropertyName("id") var id: String = UUID.randomUUID().toString(),
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("totalTax") @set:PropertyName("totalIncome") var totalTax: Double = 0.0,
    @get:PropertyName("totalIncome") @set:PropertyName("totalIncome") var totalIncome: Double = 0.0,
    @get:PropertyName("totalPAYE") @set:PropertyName("totalPAYE") var totalPAYE: Double = 0.0,
    @get:PropertyName("totalUSC") @set:PropertyName("totalUSC") var totalUSC: Double = 0.0,
    @get:PropertyName("totalPRSI") @set:PropertyName("totalPRSI") var totalPRSI: Double = 0.0,
    @get:PropertyName("taxCredit") @set:PropertyName("taxCredit") var taxCredit: Double = 4000.0,
    @get:PropertyName("tuitionFeeRelief") @set:PropertyName("tuitionFeeRelief") var tuitionFeeRelief: Double = 0.0,
    @get:PropertyName("rentTaxCredit") @set:PropertyName("rentTaxCredit") var rentTaxCredit: Double = 0.0,
    @get:PropertyName("expectedPAYE") @set:PropertyName("expectedPAYE") var expectedPAYE: Double = 0.0,
    @get:PropertyName("expectedUSC") @set:PropertyName("expectedUSC") var expectedUSC: Double = 0.0,
    @get:PropertyName("expectedPRSI") @set:PropertyName("expectedPRSI") var expectedPRSI: Double = 0.0,
    @get:PropertyName("expectedTotalTax") @set:PropertyName("expectedTotalTax") var expectedTotalTax: Double = 0.0,
    @get:PropertyName("currentWeek") @set:PropertyName("currentWeek") var currentWeek: Int = 0,
    @get:PropertyName("overpaidTax") @set:PropertyName("overpaidTax") var overpaidTax: Double = 0.0,
    @get:PropertyName("underpaidTax") @set:PropertyName("underpaidTax") var underpaidTax: Double = 0.0,
    @get:PropertyName("lastCalculated") @set:PropertyName("lastCalculated") var lastCalculated: Long = System.currentTimeMillis()
)