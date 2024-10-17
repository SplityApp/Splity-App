package com.igorj.splity.model.auth

data class AuthRegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val phoneNumber: String
)
