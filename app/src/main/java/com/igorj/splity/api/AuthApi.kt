package com.igorj.splity.api

import com.igorj.splity.model.auth.AuthLoginRequest
import com.igorj.splity.model.auth.AuthLoginResponse
import com.igorj.splity.model.auth.AuthRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("user-sign-in")
    suspend fun login(@Body request: AuthLoginRequest): Response<AuthLoginResponse>

    @POST("user-sign-up")
    suspend fun register(@Body request: AuthRegisterRequest): Response<Unit>

    @PATCH("user-reset-password")
    suspend fun userResetPassword(@Body request: UserResetPasswordRequest): Response<UserResetPasswordResponse>
}

data class CreateGroupRequest(
    val name: String,
    val currency: String
)

data class UserResetPasswordRequest(
    val email: String
)

data class UserResetPasswordResponse(
    val message: String
)
