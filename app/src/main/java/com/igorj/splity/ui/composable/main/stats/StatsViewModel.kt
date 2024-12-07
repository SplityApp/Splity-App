package com.igorj.splity.ui.composable.main.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.StatsApi
import com.igorj.splity.api.StatsBetweenDatesRequest
import com.igorj.splity.model.main.stats.StatsCategoryState
import com.igorj.splity.model.main.stats.StatsMonthlyState
import com.igorj.splity.util.LoadingController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsApi: StatsApi
): ViewModel() {
    private val _monthlyStats = MutableStateFlow<StatsMonthlyState>(StatsMonthlyState.Loading)
    val monthlyStats = _monthlyStats

    private val _categoryStats = MutableStateFlow<StatsCategoryState>(StatsCategoryState.Loading)
    val categoryStats = _categoryStats

    fun getMonthlyStats(startDate: String, endDate: String) {
        viewModelScope.launch {
            LoadingController.showLoading()
            try {
                val response = statsApi.getStatsBetweenDates(StatsBetweenDatesRequest(startDate, endDate))
                _monthlyStats.value = StatsMonthlyState.Success(response)
            } catch (e: Exception) {
                _monthlyStats.value = StatsMonthlyState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }

    fun getCategoryStats(startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                LoadingController.showLoading()
                val response = statsApi.getStatsBetweenDatesByCategory(StatsBetweenDatesRequest(startDate, endDate))
                _categoryStats.value = StatsCategoryState.Success(response)
            } catch (e: Exception) {
                _categoryStats.value = StatsCategoryState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }
}