package com.igorj.splity.api

import retrofit2.http.POST

interface StatsApi {
    @POST("get-stats")
    suspend fun getStats()
}