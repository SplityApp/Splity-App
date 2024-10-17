package com.igorj.splity.model.auth

import androidx.compose.runtime.Stable

@Stable
data class SignUpState(
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val repeatPassword: String = "",
)
