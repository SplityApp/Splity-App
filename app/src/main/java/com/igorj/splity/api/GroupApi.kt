package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.groupDetails.GroupDetails
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupApi {
    @POST("group-details")
    suspend fun getGroupDetails(@Body request: GetGroupDetailsRequest): GroupDetails
}

data class GetGroupDetailsRequest(
    @SerializedName("group_id")
    val groupId: String
)