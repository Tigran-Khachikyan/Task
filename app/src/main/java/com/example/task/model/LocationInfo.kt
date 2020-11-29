package com.example.task.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationInfo(

    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val city: String?,
    val region: String?,
    val country: String?,
) : Parcelable