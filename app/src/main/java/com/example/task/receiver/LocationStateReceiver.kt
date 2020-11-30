package com.example.task.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import com.example.task.ui.viewmodels.LocationInfoViewModel

class LocationStateReceiver(
    private val viewModel: LocationInfoViewModel
) : BroadcastReceiver() {

    private val intentFilter by lazy {
        IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION).apply {
            addAction(Intent.ACTION_PROVIDER_CHANGED)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == action) {
                context?.run {
                    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val isNetworkEnabled =
                        manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    if (isGpsEnabled || isNetworkEnabled) viewModel.enableLocation()
                }
            }
        }


    }

    fun register(context: Context) {
        context.registerReceiver(this, intentFilter)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }
}