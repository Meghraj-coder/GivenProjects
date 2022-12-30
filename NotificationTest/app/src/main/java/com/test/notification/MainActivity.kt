package com.test.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "Corona13"
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // will see later if needed
            } else {
                Toast.makeText(
                    this,
                    "You've denied notification permission! Pls allow it from the settings to get the best feature of this app.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermission()

        val pref = getSharedPreferences("com.test.notif", MODE_PRIVATE)
        val fcmToken = pref.getString("fcmToken", null)
        if (fcmToken == null) {
            MyFuctions.getToken(this)
        } else {
            val tv = findViewById<TextView>(R.id.tv)
            tv.text = fcmToken
            Log.d("AAAAAA", fcmToken)
            tv.setOnClickListener {
                copy(fcmToken)
            }
        }
    }

    private fun copy(text: String) {
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied Successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                    AlertDialog.Builder(this).setMessage(
                        "Please Grant the permissions to send you notification. Otherwise" +
                                "the main functionality of this app is dismissed."
                    ).setTitle("Permission Requires")
                        .setNegativeButton("No thanks", null)
                        .setPositiveButton("OK") { _, _ ->
                            askNotificationPermission()
                        }
                } else {
                    // Directly asking for the permission
                    requestPermissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }
        }
    }
}