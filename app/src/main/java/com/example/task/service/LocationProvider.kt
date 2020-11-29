package com.example.task.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.task.INTERVAL_LOCATION_REQUEST
import com.example.task.model.LocationInfo
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.lang.Exception
import java.util.*

class LocationProvider(private val context: Context) {

    private val locationRequest: LocationRequest = LocationRequest.create()
        .setInterval(INTERVAL_LOCATION_REQUEST)
        .setFastestInterval(500)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    @SuppressLint("MissingPermission")
    fun getDeviceLocationForGrantedPermissions(onLocationReceiver: (LocationInfo) -> Unit) {

        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {

                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)



                    locationResult?.run {
                        for (loc in locations) {
                            loc?.let {
                                val address = getAddressFromLocation(it)
                                address?.run {
                                    val streetAdd: String? = try {
                                        getAddressLine(0).split(",")[0]
                                    } catch (ex: Exception) {
                                        null
                                    }
                                    onLocationReceiver(
                                        LocationInfo(
                                            latitude,
                                            longitude,
                                            streetAdd,
                                            locality,
                                            adminArea,
                                            countryName
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }, null)
    }


    private fun getAddressFromLocation(location: Location): Address? {
        val geoCoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}