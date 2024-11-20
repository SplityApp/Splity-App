package com.igorj.splity.model.main.expense

sealed interface ExpenseState {
    data object Loading : ExpenseState
    data class Success(val expenses: List<Expense>) : ExpenseState
    data class Error(val message: String) : ExpenseState
}