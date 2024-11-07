package com.igorj.splity.model.main.expense

enum class ExpenseCategory(val emoji: String, val title: String) {
    ALCOHOL("\uD83C\uDF7A", "Alcohol"), // 🍺
    FOOD("\uD83C\uDF54", "Food"), // 🍔
    TRANSPORT("\uD83D\uDE8C", "Transport"), // 🚌
    ACCOMMODATION("\uD83C\uDFE8", "Accommodation"), // 🏨
    ENTERTAINMENT("\uD83C\uDFAC", "Entertainment"), // 🎬
    SHOPPING("\uD83D\uDED2", "Shopping"), // 🛒
    OTHER("\uD83D\uDCB0", "Other"), // 💰
    UNKNOWN("❓", "")
}