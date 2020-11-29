package com.example.task.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.task.ACTION_INTENT_SEND_LOCATION_INFO
import com.example.task.ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK
import com.example.task.KEY_DATA_LOCATION_INFO
import com.example.task.R
import com.example.task.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class TaskService : Service(), CoroutineScope {

    private val job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
            .apply { action = ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_trash_mini)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        LocationProvider(this).getDeviceLocationForGrantedPermissions {

            val intentLocInfo = Intent(ACTION_INTENT_SEND_LOCATION_INFO)
                .apply { putExtra(KEY_DATA_LOCATION_INFO, it) }
            sendBroadcast(intentLocInfo)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}