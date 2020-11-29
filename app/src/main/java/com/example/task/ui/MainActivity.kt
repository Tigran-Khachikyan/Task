package com.example.task.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import com.example.task.*
import com.example.task.ui.viewmodels.PermissionsViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PermissionsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

   /*     intent.action?.let {
            if (it == ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK)
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_home, Bundle().apply {
                    putBoolean(BUNDLE_KEY_FROM_NOTIFICATION_CLICK, true)
                })
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                viewModel.grantPermissionWriteStorage()
        }

        if (requestCode == REQUEST_CODE_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty())
                run breaker@{
                    grantResults.forEach {
                        if (it == PackageManager.PERMISSION_GRANTED) {
                            viewModel.grantPermissionLocation()
                            return@breaker
                        }
                    }
                }
        }
    }

}