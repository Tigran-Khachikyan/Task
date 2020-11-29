package com.example.task.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.task.R
import com.example.task.REQUEST_PERMISSION_CODE
import com.example.task.ui.viewmodels.ActivityViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by lazy {
        ViewModelProvider(this).get(ActivityViewModel::class.java)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                viewModel.grantPermission()
        }
    }

}