package com.igorj.splity.model.main.balance

sealed interface BalanceState {
    data object Loading : BalanceState
    data class Success(val balancesResponse: BalanceResponse) : BalanceState
    data class Error(val message: String) : BalanceState
}