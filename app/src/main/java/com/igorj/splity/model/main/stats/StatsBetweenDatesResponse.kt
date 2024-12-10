package com.igorj.splity.model.main.stats

import com.google.gson.annotations.SerializedName

data class StatsBetweenDatesResponse(
    val date: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
)