package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ExpenseApi {

    @POST("add-expense")
    suspend fun addExpense(@Body request: AddExpenseRequest): Response<Unit>
}

data class AddExpenseRequest(
    @SerializedName("paid_by")
    val paidBy: String,
    @SerializedName("group_id")
    val groupId: String,
    val description: String,
    val category: String,
    val amount: Double,
    val splits: List<Split>
)

data class Split(
    @SerializedName("user_id")
    val userId: String,
    val amount: Double
)
