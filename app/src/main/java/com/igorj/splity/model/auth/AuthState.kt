package com.igorj.splity.model.auth

sealed class AuthState {
    data object Initial : AuthState()
    data object Authenticated : AuthState()
}
