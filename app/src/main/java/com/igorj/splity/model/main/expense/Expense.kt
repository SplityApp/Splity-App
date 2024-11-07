package com.igorj.splity.model.main.expense

import com.google.gson.annotations.SerializedName

data class Expense(
    val id: String,
    val description: String,
    val category: String,
    val amount: Double,
    val state: String,
    @SerializedName("paid_by")
    val paidBy: String,
)
