package com.igorj.splity.model.main.stats

sealed class StatsCurrenciesState {
    data object Loading : StatsCurrenciesState()
    data class Success(val currencies: List<String>) : StatsCurrenciesState()
    data class Error(val message: String) : StatsCurrenciesState()
}