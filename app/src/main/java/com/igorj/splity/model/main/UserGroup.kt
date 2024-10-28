package com.igorj.splity.model.main

import java.sql.Timestamp

data class UserGroup(
    val id: String,
    val name: String,
    val currency: String,
    val createdAt: Timestamp,
    val totalAmount: Double,
)
