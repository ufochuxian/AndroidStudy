package com.eric.base.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalculationResult(
    val result: Int,
    val description: String
) : Parcelable