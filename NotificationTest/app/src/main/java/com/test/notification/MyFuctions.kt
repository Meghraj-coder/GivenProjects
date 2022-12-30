package com.test.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFuctions {

    companion object {
        private const val TAG: String = "AAAAAA"

        fun getToken(ctx: Context) {
            val fcm = FirebaseMessaging.getInstance()
            fcm.token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d(TAG, "token: $token")
                ctx.getSharedPreferences("com.test.notif", AppCompatActivity.MODE_PRIVATE)
                    .edit().putString("fcmToken",token).apply()
                addToDb(token)
                createNotificationChannel(ctx)
            })
            fcm.subscribeToTopic("Corona")
        }

        private fun addToDb(token: String?) {
            //will code this if custom api via browser is needed to push notification
        }

        private fun createNotificationChannel(ctx: Context) {
            // Since android Oreo notification channel is needed.
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = ctx.getString(R.string.channel_name)
                val descriptionText = ctx.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(MainActivity.CHANNEL_ID, name, importance)
                mChannel.description = descriptionText
                val notificationManager = ctx.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }
}