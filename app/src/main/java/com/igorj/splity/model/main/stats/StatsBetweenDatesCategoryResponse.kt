package com.igorj.splity.model.main.stats

import com.google.gson.annotations.SerializedName

data class StatsBetweenDatesCategoryResponse(
    val category: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
)
