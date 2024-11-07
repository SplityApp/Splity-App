package com.igorj.splity.model.main

import com.google.gson.JsonParser

data class ErrorResponse(
    val message: String
)

fun errorResponse(message: String?): ErrorResponse {
    return try {
        val jsonObject = JsonParser().parse(message ?: "").asJsonObject
        ErrorResponse(
            message = jsonObject.get("message")?.asString ?: "Unknown error"
        )
    } catch (e: Exception) {
        ErrorResponse(message = "Error parsing response")
    }
}
