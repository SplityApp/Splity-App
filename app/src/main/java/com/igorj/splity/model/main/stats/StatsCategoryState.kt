package com.igorj.splity.model.main.stats

sealed class StatsCategoryState {
    object Loading : StatsCategoryState()
    data class Success(val stats: List<StatsBetweenDatesCategoryResponse>) : StatsCategoryState()
    data class Error(val message: String) : StatsCategoryState()
}