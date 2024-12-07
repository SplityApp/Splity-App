package com.igorj.splity.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.igorj.splity.api.FcmApi
import com.igorj.splity.api.SetFcmTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PushNotificationService: FirebaseMessagingService() {

    private val fcmApi: FcmApi by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = SetFcmTokenRequest(fcmToken = token)
                val response = fcmApi.setFcmToken(request)

                if (response.isSuccessful) {
                    Log.d("LOGCAT PushNotificationService", "Token successfully updated on server")
                } else {
                    Log.e("LOGCAT PushNotificationService", "Failed to update token: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LOGCAT PushNotificationService", "Error updating token", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, notification)

        Log.d("LOGCAT", "Notification received")
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Splity"
    }
}
