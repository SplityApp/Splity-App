package com.igorj.splity.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
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
                    Log.d("FCM", "Token successfully updated on server")
                } else {
                    Log.e("FCM", "Failed to update token: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error updating token", e)
            }
        }
    }
}
