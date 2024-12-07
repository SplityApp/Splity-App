package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.stats.StatsBetweenDatesResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface StatsApi {
    @POST("expenses-between-dates")
    suspend fun getStatsBetweenDates(@Body request: StatsBetweenDatesRequest): List<StatsBetweenDatesResponse>
}

data class StatsBetweenDatesRequest(
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String
)
