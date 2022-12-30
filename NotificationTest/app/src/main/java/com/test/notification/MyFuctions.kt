package com.test.notification

import android.content.Context
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
            })
            fcm.subscribeToTopic("Corona")
        }

        private fun addToDb(token: String?) {
            //will code this if custom api via browser is needed to push notification
        }
    }
}