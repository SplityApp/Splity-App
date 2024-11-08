package com.igorj.splity.model.main

import com.google.gson.annotations.SerializedName

data class GroupProfile(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("group_id")
    val groupId: String,
)
