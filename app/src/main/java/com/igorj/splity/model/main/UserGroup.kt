package com.igorj.splity.model.main

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class UserGroup(
    val id: String,
    val name: String,
    val currency: String,
    @SerializedName("created_at")
    val createdAt: Timestamp,
    @SerializedName("my_balance")
    val myBalance: Double,
)
