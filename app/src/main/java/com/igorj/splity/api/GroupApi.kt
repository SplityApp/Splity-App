package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.balance.BalanceResponse
import com.igorj.splity.model.main.expense.Expense
import com.igorj.splity.model.main.groupDetails.GroupDetails
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupApi {
    @POST("group-details")
    suspend fun getGroupDetails(@Body request: GetGroupDetailsRequest): GroupDetails

    @POST("get-balances")
    suspend fun getBalances(@Body request: GetBalancesRequest): BalanceResponse

    @POST("group-expenses")
    suspend fun getExpenses(@Body request: GetExpensesRequest): List<Expense>
}

data class GetGroupDetailsRequest(
    @SerializedName("group_id")
    val groupId: String
)

data class GetBalancesRequest(
    @SerializedName("group_id")
    val groupId: String
)

data class GetExpensesRequest(
    @SerializedName("group_id")
    val groupId: String
)