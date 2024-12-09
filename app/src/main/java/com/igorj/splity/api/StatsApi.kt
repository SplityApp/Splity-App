package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.stats.StatsBetweenDatesCategoryResponse
import com.igorj.splity.model.main.stats.StatsBetweenDatesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StatsApi {
    @POST("expenses-between-dates")
    suspend fun getStatsBetweenDates(@Body request: StatsBetweenDatesRequest): List<StatsBetweenDatesResponse>

    @POST("expenses-by-category")
    suspend fun getStatsBetweenDatesByCategory(@Body request: StatsBetweenDatesRequest): List<StatsBetweenDatesCategoryResponse>

    @GET("user-currencies")
    suspend fun getUserCurrencies(): List<String>
}

data class StatsBetweenDatesRequest(
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val currency: String
)
