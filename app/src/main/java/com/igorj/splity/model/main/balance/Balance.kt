package com.igorj.splity.model.main.balance

import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("request_user")
    val requestUser: Balance,
    val users: List<Balance>,
)

data class Balance(
    val id: String,
    val balance: Double,
    val name: String
)
