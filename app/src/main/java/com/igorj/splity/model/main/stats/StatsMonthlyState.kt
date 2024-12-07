package com.igorj.splity.model.main.stats

sealed class StatsMonthlyState {
    data object Loading : StatsMonthlyState()
    data class Success(val stats: List<StatsBetweenDatesResponse>) : StatsMonthlyState()
    data class Error(val message: String) : StatsMonthlyState()
}