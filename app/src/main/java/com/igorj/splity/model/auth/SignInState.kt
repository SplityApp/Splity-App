package com.igorj.splity.model.auth

import androidx.compose.runtime.Stable

@Stable
data class SignInState(
    val email: String = "",
    val password: String = "",
)
