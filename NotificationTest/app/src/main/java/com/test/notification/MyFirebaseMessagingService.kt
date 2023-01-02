package com.test.notification

import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.test.notification.MainActivity.Companion.CHANNEL_ID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_ID = 13
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val pref = getSharedPreferences("com.test.notif", MODE_PRIVATE)
        pref.edit().putString("fcmToken", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = "New Corona is coming."
        val body = "New Year brings new wave of Corona. Happy New Year 2024!"
        val bmp = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.large_icon)
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_corona)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(body))
            .setContentText(body)
            .setColor(resources.getColor(R.color.not))
            .setLargeIcon(bmp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        //showing the notif
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}
