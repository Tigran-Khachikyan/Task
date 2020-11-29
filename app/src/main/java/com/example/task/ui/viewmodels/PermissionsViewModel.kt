package com.example.task.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {

    private val permissionWriteStorage by lazy { MutableLiveData<Boolean>() }
    fun grantPermissionWriteStorage() {
        permissionWriteStorage.value = true
    }

    fun isStoragePermissionGranted(): LiveData<Boolean> = permissionWriteStorage

    private val permissionLocation by lazy { MutableLiveData<Boolean>() }
    fun grantPermissionLocation() {
        permissionLocation.value = true
    }

    fun isLocationPermissionGranted(): LiveData<Boolean> = permissionLocation
}