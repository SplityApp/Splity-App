package com.igorj.splity

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.AuthApi
import com.igorj.splity.model.auth.AuthLoginRequest
import com.igorj.splity.model.auth.AuthRegisterRequest
import com.igorj.splity.model.auth.AuthState
import com.igorj.splity.model.auth.SignUpState
import com.igorj.splity.util.auth.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
        if (!validateLogin(email, password)) {
            Log.e("AuthViewModel", "Invalid login credentials")
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val response = api.login(AuthLoginRequest(email, password))
                if (response.token.isNotBlank()) {
                    tokenManager.saveToken(response.token)
                    _authState.value = AuthState.Authenticated
                } else {
                    Log.e("AuthViewModel", "Invalid token received")
                }
            } catch (e: HttpException) {
                Log.e("AuthViewModel", "HTTP Error: ${e.message()}", e)
            } catch (e: IOException) {
                Log.e("AuthViewModel", "Network Error: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Unexpected Error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(signUpState: SignUpState) {
        if (!validateRegistration(signUpState)) {
            Log.e("AuthViewModel", "Invalid registration data")
            return
        }
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val request = AuthRegisterRequest(
                    email = signUpState.email,
                    password = signUpState.password,
                    username = signUpState.username,
                    phoneNumber = signUpState.phoneNumber
                )
                api.register(request)
                Log.d("AuthViewModel", "Registration successful")
            } catch (e: HttpException) {
                Log.e("AuthViewModel", "HTTP Error during registration: ${e.message()}", e)
            } catch (e: IOException) {
                Log.e("AuthViewModel", "Network Error during registration: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Unexpected Error during registration: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validateRegistration(signUpState: SignUpState): Boolean {
        return signUpState.username.isNotBlank() &&
                isValidEmail(signUpState.email) &&
                isValidPhoneNumber(signUpState.phoneNumber) &&
                isValidPassword(signUpState.password) &&
                signUpState.password == signUpState.repeatPassword
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val formatWithCountryCode = "^\\+48\\d{9}$".toRegex()
        val formatJustDigits = "^\\d{9}$".toRegex()

        return when {
            phoneNumber.matches(formatWithCountryCode) -> true
            phoneNumber.matches(formatJustDigits) -> true
            else -> false
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8  // TODO (or just leave all for backend)
    }

    private fun validateLogin(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    fun logout() {
        tokenManager.clearToken()
        _authState.value = AuthState.Initial
    }
}
