package com.igorj.splity.util.auth

import android.content.SharedPreferences

class TokenManager(private val sharedPreferences: SharedPreferences) {
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_AUTH_KEY, token).apply()
    }
    
    fun getToken(): String? = sharedPreferences.getString(TOKEN_AUTH_KEY, null)
    
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_AUTH_KEY).apply()
    }

    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_REFRESH, token).apply()
    }

    fun getRefreshToken(): String? = sharedPreferences.getString(TOKEN_REFRESH, null)

    fun clearRefreshToken() {
        sharedPreferences.edit().remove(TOKEN_REFRESH).apply()
    }
}

const val TOKEN_AUTH_KEY = "token_auth_key"
const val TOKEN_REFRESH = "token_refresh"
val TOKEN_REFRESH_ENDPOINTS = arrayOf("change-user-info")

