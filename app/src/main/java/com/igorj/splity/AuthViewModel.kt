package com.igorj.splity

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.igorj.splity.api.AuthApi
import com.igorj.splity.api.FcmApi
import com.igorj.splity.api.SetFcmTokenRequest
import com.igorj.splity.api.UserResetPasswordRequest
import com.igorj.splity.model.auth.AuthLoginRequest
import com.igorj.splity.model.auth.AuthRegisterRequest
import com.igorj.splity.model.auth.AuthState
import com.igorj.splity.model.auth.SignUpState
import com.igorj.splity.model.main.errorResponse
import com.igorj.splity.repository.UserInfoRepository
import com.igorj.splity.util.LoadingController
import com.igorj.splity.util.SnackbarConfig
import com.igorj.splity.util.SnackbarController
import com.igorj.splity.util.SnackbarEvent
import com.igorj.splity.util.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val api: AuthApi,
    private val fcmApi: FcmApi,
    private val tokenManager: TokenManager,
    private val userInfoRepository: UserInfoRepository
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

    init {
        tokenManager.getToken()?.let {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                LoadingController.showLoading()
                userInfoRepository.clearUserInfo()
                val response = api.login(AuthLoginRequest(email, password))
                if (response.isSuccessful && response.body()?.token != null) {
                    val token = response.body()!!.token
                    val refreshToken = response.body()!!.refreshToken

                    tokenManager.saveToken(token)
                    tokenManager.saveRefreshToken(refreshToken)
                    SnackbarController.showSnackbar(
                        SnackbarEvent(
                            message = "Login successful",
                            config = SnackbarConfig(backgroundColor = Color.Green)
                        )
                    )
                    setFcmToken()
                    _authState.value = AuthState.Authenticated
                } else {
                    val error = errorResponse(response.errorBody()?.string())
                    SnackbarController.showSnackbar(
                        SnackbarEvent(
                            message = error.message,
                            config = SnackbarConfig(backgroundColor = Color.Red)
                        )
                    )
                }
            } catch (e: Exception) {
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = e.message ?: "An error occurred",
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
            } finally {
                LoadingController.hideLoading()
            }
        }
    }

    suspend fun register(signUpState: SignUpState): Result<Unit> {
        return try {
            LoadingController.showLoading()
            val request = AuthRegisterRequest(
                email = signUpState.email,
                password = signUpState.password,
                username = signUpState.username,
                phoneNumber = signUpState.phoneNumber
            )
            val response = api.register(request)
            if (response.isSuccessful) {
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = "Registration successful",
                        config = SnackbarConfig(backgroundColor = Color.Green)
                    )
                )
                Result.success(Unit)
            } else {
                val error = errorResponse(response.errorBody()?.string())
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = error.message,
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
                Result.failure(Exception(error.message))
            }
        } catch (e: Exception) {
            SnackbarController.showSnackbar(
                SnackbarEvent(
                    message = e.message ?: "An error occurred",
                    config = SnackbarConfig(backgroundColor = Color.Red)
                )
            )
            Result.failure(e)
        } finally {
            LoadingController.hideLoading()
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                LoadingController.showLoading()
                val response = api.userResetPassword(UserResetPasswordRequest(email))
                if (response.isSuccessful) {
                    SnackbarController.showSnackbar(
                        SnackbarEvent(
                            message = "Password reset email sent",
                            config = SnackbarConfig(backgroundColor = Color.Green)
                        )
                    )
                } else {
                    val error = errorResponse(response.errorBody()?.string())
                    SnackbarController.showSnackbar(
                        SnackbarEvent(
                            message = error.message,
                            config = SnackbarConfig(backgroundColor = Color.Red)
                        )
                    )
                }
            } catch (e: Exception) {
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = e.message ?: "An error occurred",
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
            } finally {
                LoadingController.hideLoading()
            }
        }
    }

//    private fun validateRegistration(signUpState: SignUpState): Boolean {
//        return signUpState.username.isNotBlank() &&
//                isValidEmail(signUpState.email) &&
//                isValidPhoneNumber(signUpState.phoneNumber) &&
//                isValidPassword(signUpState.password) &&
//                signUpState.password == signUpState.repeatPassword
//    }
//
//    private fun isValidEmail(email: String): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//
//    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
//        val formatWithCountryCode = "^\\+48\\d{9}$".toRegex()
//        val formatJustDigits = "^\\d{9}$".toRegex()
//
//        return when {
//            phoneNumber.matches(formatWithCountryCode) -> true
//            phoneNumber.matches(formatJustDigits) -> true
//            else -> false
//        }
//    }
//
//    private fun isValidPassword(password: String): Boolean {
//        return password.length >= 8  // TODO (or just leave all for backend)
//    }
//
//    private fun validateLogin(email: String, password: String): Boolean {
//        return email.isNotBlank() && password.isNotBlank()
//    }

    fun setFcmToken() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fcmToken = Firebase.messaging.token.await()
                val request = SetFcmTokenRequest(fcmToken = fcmToken)
                val response = fcmApi.setFcmToken(request)

                if (response.isSuccessful) {
                    Log.d("LOGCAT", "Token successfully updated on server")
                } else {
                    Log.e("LOGCAT", "Failed to update token: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LOGCAT", "Error updating token", e)
            }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        tokenManager.clearRefreshToken()
        userInfoRepository.clearUserInfo()
        _authState.value = AuthState.Initial
    }
}
