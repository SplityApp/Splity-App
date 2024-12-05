package com.igorj.splity.util.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = chain.request().newBuilder()
        
        tokenManager.getToken()?.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }

        tokenManager.getRefreshToken()?.let { refreshToken ->
            if (TOKEN_REFRESH_ENDPOINTS.any { originalRequest.url().encodedPath().contains(it) }) {
                request.addHeader("Refresh-Token", "Bearer $refreshToken")
            }
        }
        
        return chain.proceed(request.build())
    }
}
