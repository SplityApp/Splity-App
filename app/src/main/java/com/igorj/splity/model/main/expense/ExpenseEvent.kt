package com.igorj.splity.model.main.expense

sealed class ExpenseEvent {
    data object ExpenseAdded: ExpenseEvent()
}
