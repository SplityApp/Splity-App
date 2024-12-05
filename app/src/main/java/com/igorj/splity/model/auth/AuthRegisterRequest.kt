package com.igorj.splity.model.auth

import com.google.gson.annotations.SerializedName

data class AuthRegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)
