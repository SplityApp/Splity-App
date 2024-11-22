package com.igorj.splity.model.main

import com.google.gson.annotations.SerializedName

data class UserInfo(
    val id: String = "",
    val email: String = "",
    @SerializedName("phone_number")
    val phoneNumber: String = "",
    val username: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("char_image")
    val charImage: String = "",
    @SerializedName("allowed_notifications")
    val allowedNotifications: Boolean = false,
)