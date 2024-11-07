package com.igorj.splity.model.main.groupDetails

data class Expense(
    val id: String,
    val description: String,
    val category: String,
    val amount: Double,
    val state: String,
)
