package com.example.urbanmessenger.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.urbanmessenger.MainActivity
import com.example.urbanmessenger.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var _token: String? = null
    private val token get() = _token!!

    companion object {
        const val COUNTER_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }


    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        //senTokenToAppServer
        Log.d("@@@", "${sendRegistrationToServer(token)}")
    }


    @SuppressLint("MissingPermission", "NotificationPermission")
    private fun generateNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Уведомления", importance)
            val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notify(COUNTER_ID, builder.build())
                    return@with
                }
            }

        }

    }
}


