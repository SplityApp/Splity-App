package com.igorj.splity.api

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
}
