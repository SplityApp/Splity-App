package com.igorj.splity.model.main.groupDetails

import com.google.gson.annotations.SerializedName
import com.igorj.splity.model.main.UserInfo
import java.sql.Timestamp

data class GroupDetails(
    val id: String,
    val name: String,
    val currency: String,
    @SerializedName("created_at")
    val createdAt: Timestamp,
    @SerializedName("invite_code")
    val inviteCode: String,
    val profiles: List<UserInfo>,
    @SerializedName("updated_at")
    val updatedAt: Timestamp
)