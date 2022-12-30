package com.test.notification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import com.test.notification.MainActivity.Companion.CHANNEL_ID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "BBBBBB"
    private val NOTIFICATION_ID = 13
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val pref = getSharedPreferences("com.test.notif", MODE_PRIVATE)
        pref.edit().putString("fcmToken", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the message here: 3 types: notification, data, both
        val notification = remoteMessage.notification
        val title = notification?.title+""
        val body = notification?.body+""
        val url = notification?.imageUrl

        if (url == null) sendNotif(title, body, null)
        else Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                sendNotif(title, body, bitmap)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                sendNotif(title, body, null)
            }
        })
    }

    private fun sendNotif(title:String, body:String, bitmap: Bitmap?) {
        val bmp = bitmap ?: BitmapFactory.decodeResource(applicationContext.resources, R.drawable.large_icon)
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_corona)
            .setContentTitle(title)
            .setContentText(body)
            .setLargeIcon(bmp)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bmp)
                .bigLargeIcon(null))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        //showing the notif
        with(NotificationManagerCompat.from(applicationContext)) {notify(NOTIFICATION_ID, builder.build()) }
    }
}
