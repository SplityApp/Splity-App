package com.igorj.splity.api

import com.igorj.splity.model.main.UserGroupsResponse
import retrofit2.http.GET

interface HomeApi {
    @GET("user-groups")
    suspend fun getUserGroups(): UserGroupsResponse
}
