package com.igorj.splity

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.AuthApi
import com.igorj.splity.model.auth.AuthLoginRequest
import com.igorj.splity.model.auth.AuthRegisterRequest
import com.igorj.splity.model.auth.AuthState
import com.igorj.splity.model.auth.SignUpState
import com.igorj.splity.model.main.errorResponse
import com.igorj.splity.util.SnackbarConfig
import com.igorj.splity.util.SnackbarController
import com.igorj.splity.util.SnackbarEvent
import com.igorj.splity.util.auth.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val api: AuthApi,
    private val tokenManager: TokenManager
): ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        tokenManager.getToken()?.let {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val response = api.login(AuthLoginRequest(email, password))
            if (response.isSuccessful && response.body()?.token != null) {
                val token = response.body()!!.token
                tokenManager.saveToken(token)
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = "Login successful",
                        config = SnackbarConfig(backgroundColor = Color.Green)
                    )
                )
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
            
            _isLoading.value = false
        }
    }

    fun register(signUpState: SignUpState) {
        viewModelScope.launch {
            _isLoading.value = true

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
            } else {
                val error = errorResponse(response.errorBody()?.string())
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = error.message,
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
            }
            
            _isLoading.value = false
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

    fun logout() {
        tokenManager.clearToken()
        _authState.value = AuthState.Initial
    }
}
