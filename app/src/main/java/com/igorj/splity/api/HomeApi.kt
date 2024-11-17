package com.igorj.splity.api

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.UserGroupsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeApi {
    @GET("user-groups")
    suspend fun getUserGroups(): Response<UserGroupsResponse>

    @POST("create-group")
    suspend fun createGroup(@Body createGroupRequest: CreateGroupRequest): Response<Unit>

    @POST("add-user-to-group")
    suspend fun joinGroup(@Body joinGroupRequest: JoinGroupRequest): Response<Unit>
}

data class JoinGroupRequest(
    @SerializedName("invite_code")
    val inviteCode: String
)
