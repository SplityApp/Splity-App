package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("set-fcm-token")
    suspend fun setFcmToken(@Body request: SetFcmTokenRequest): Response<Unit>

    @POST("push-notifications")
    suspend fun pushNotification(@Body request: PushNotificationRequest): Response<Unit>

}

data class SetFcmTokenRequest(
    @SerializedName("fcm_token")
    val fcmToken: String
)

data class PushNotificationRequest(
    @SerializedName("user_id")
    val userId: String,
    val title: String,
    val description: String
)
