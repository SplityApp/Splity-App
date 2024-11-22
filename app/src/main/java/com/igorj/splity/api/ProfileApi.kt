package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {

    @GET("get-user-info")
    suspend fun getUserInfo(): Response<UserInfo>

    @PATCH("change-notifications")
    suspend fun updateNotificationPreferences(
        @Body request: ChangeNotificationsRequest
    ): Response<ChangeNotificationsResponse>
}

data class ChangeNotificationsRequest(
    @SerializedName("allowed_notifications")
    val allowedNotifications: Boolean
)

data class ChangeNotificationsResponse(
    @SerializedName("allowed_notifications")
    val allowedNotifications: Boolean
)
