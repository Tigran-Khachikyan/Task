package com.example.task.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.task.ACTION_INTENT_SEND_LOCATION_INFO
import com.example.task.ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK
import com.example.task.KEY_DATA_LOCATION_INFO
import com.example.task.R
import com.example.task.ui.MainActivity


class TaskService : Service() {

    private val channelId = "ForegroundServiceChannel"
    private lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        createAudioPlay()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
            .apply { action = ACTION_OPEN_LOCATION_ON_NOTIFICATION_CLICK }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Task Service")
            .setContentText("Play music and get current location!")
            .setSmallIcon(R.drawable.ic_start)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)


        player.start()
        LocationProvider(this).getDeviceLocationForGrantedPermissions {

            val intentLocInfo = Intent(ACTION_INTENT_SEND_LOCATION_INFO)
                .apply { putExtra(KEY_DATA_LOCATION_INFO, it) }
            sendBroadcast(intentLocInfo)
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        player.stop()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createAudioPlay() {
        player = MediaPlayer.create(applicationContext, R.raw.fly)
        player.isLooping = true
    }
}