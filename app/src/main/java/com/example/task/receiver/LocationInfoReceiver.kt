package com.example.task.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.task.ACTION_INTENT_SEND_LOCATION_INFO
import com.example.task.KEY_DATA_LOCATION_INFO
import com.example.task.model.LocationInfo
import com.example.task.ui.viewmodels.LocationInfoViewModel

class LocationInfoReceiver(
    private val viewModel: LocationInfoViewModel
) : BroadcastReceiver() {

    private val intentFilter by lazy { IntentFilter(ACTION_INTENT_SEND_LOCATION_INFO) }

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.run {
            if (action == ACTION_INTENT_SEND_LOCATION_INFO) {
                val locInfo: LocationInfo? = getParcelableExtra(KEY_DATA_LOCATION_INFO)
                locInfo?.let { viewModel.updateCurrentLocationInfo(it) }
                Log.d("as4984dab55sd6", "info")
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