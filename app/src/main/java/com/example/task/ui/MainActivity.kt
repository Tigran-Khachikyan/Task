package com.example.task.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.task.*
import com.example.task.ui.viewmodels.PermissionsViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PermissionsViewModel::class.java)
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