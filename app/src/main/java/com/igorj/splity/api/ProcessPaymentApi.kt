package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProcessPaymentApi {

    @POST("process-payment")
    suspend fun processPayment(@Body processPaymentRequest: ProcessPaymentRequest): Response<Unit>
}

data class ProcessPaymentRequest(
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("receiver_id")
    val receiverId: String,
    val amount: Double
)
