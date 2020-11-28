package com.example.task.ui.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel : ViewModel() {

    private val permissionGranted by lazy { MutableLiveData<Boolean?>() }
    fun grantPermission() {
        permissionGranted.value = true
    }

    fun permissionState(): LiveData<Boolean?> = permissionGranted
}