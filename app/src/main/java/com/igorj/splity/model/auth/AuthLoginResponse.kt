package com.igorj.splity.model.auth

import com.google.gson.annotations.SerializedName

data class AuthLoginResponse(
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)
