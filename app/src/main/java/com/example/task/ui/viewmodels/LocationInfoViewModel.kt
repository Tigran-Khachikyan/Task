package com.example.task.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.task.model.LocationInfo

class LocationInfoViewModel : ViewModel() {

    // Info update
    private val info = MutableLiveData<LocationInfo?>()
    fun updateCurrentLocationInfo(locationInfo: LocationInfo) {
        info.value = locationInfo
    }

    fun getCurrentLocationInfo(): LiveData<LocationInfo?> = info


    //GPS enable
    private val locationState = MutableLiveData<Boolean>()
    fun enableLocation() {
        locationState.value = true
    }

    fun isLocationEnabled(): LiveData<Boolean> {
        return locationState
    }
}