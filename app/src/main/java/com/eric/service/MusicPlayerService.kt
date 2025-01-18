package com.eric.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.eric.androidstudy.R
import com.eric.base.ext.ERIC_TAG

class MusicPlayerService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        Log.d(ERIC_TAG, "MusicPlayerService: onCreate called")
        mediaPlayer = MediaPlayer.create(this, R.raw.pianesque) // 替换为你的音乐文件
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(ERIC_TAG, "MusicPlayerService: onStartCommand called")
        // 创建通知并启动前台服务
        startForeground(1, createNotification("Playing music"))
        mediaPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(ERIC_TAG, "MusicPlayerService: onDestroy called")
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(contentText: String): Notification {
        val notificationChannelId = "MusicPlayerChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Music Player")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.pianesque)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
