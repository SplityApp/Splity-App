package com.igorj.splity.model.main.stats

sealed class StatsState {
    data object Loading : StatsState()
    data class Success(val stats: List<StatsBetweenDatesResponse>) : StatsState()
    data class Error(val message: String) : StatsState()
}