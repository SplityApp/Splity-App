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
}

const val TOKEN_AUTH_KEY = "token_auth_key"
