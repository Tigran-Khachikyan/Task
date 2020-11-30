package com.example.task.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {

    private val permissionWriteStorage by lazy { MutableLiveData<Unit>() }
    fun grantPermissionWriteStorage() {
        permissionWriteStorage.value = Unit
    }

    fun isStoragePermissionGranted(): LiveData<Unit> = permissionWriteStorage

    private val permissionLocation by lazy { MutableLiveData<Unit>() }
    fun grantLocationPermission() {
        permissionLocation.value = Unit
    }

    fun isLocationPermissionGranted(): LiveData<Unit> = permissionLocation
}