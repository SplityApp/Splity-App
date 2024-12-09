package com.igorj.splity.ui.composable.main.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.StatsApi
import com.igorj.splity.api.StatsBetweenDatesRequest
import com.igorj.splity.model.main.stats.StatsCategoryState
import com.igorj.splity.model.main.stats.StatsCurrenciesState
import com.igorj.splity.model.main.stats.StatsMonthlyState
import com.igorj.splity.util.LoadingController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsApi: StatsApi
): ViewModel() {
    private val _currencies = MutableStateFlow<StatsCurrenciesState>(StatsCurrenciesState.Loading)
    val currencies = _currencies

    private val _monthlyStats = MutableStateFlow<StatsMonthlyState>(StatsMonthlyState.Loading)
    val monthlyStats = _monthlyStats

    private val _categoryStats = MutableStateFlow<StatsCategoryState>(StatsCategoryState.Loading)
    val categoryStats = _categoryStats

    fun getCurrencies(initialStartDate: String, initialEndDate: String) {
        viewModelScope.launch {
            try {
                LoadingController.showLoading()
                val response = statsApi.getUserCurrencies()
                _currencies.value = StatsCurrenciesState.Success(response)
                getMonthlyStats(initialStartDate, initialEndDate, response.first())
                getCategoryStats(initialStartDate, initialEndDate, response.first())
            } catch (e: Exception) {
                _currencies.value = StatsCurrenciesState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }

    fun getMonthlyStats(startDate: String, endDate: String, currency: String) {
        viewModelScope.launch {
            LoadingController.showLoading()
            try {
                val response = statsApi.getStatsBetweenDates(StatsBetweenDatesRequest(startDate, endDate, currency))
                _monthlyStats.value = StatsMonthlyState.Success(response)
            } catch (e: Exception) {
                _monthlyStats.value = StatsMonthlyState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }

    fun getCategoryStats(startDate: String, endDate: String, currency: String) {
        viewModelScope.launch {
            try {
                LoadingController.showLoading()
                val response = statsApi.getStatsBetweenDatesByCategory(StatsBetweenDatesRequest(startDate, endDate, currency))
                _categoryStats.value = StatsCategoryState.Success(response)
            } catch (e: Exception) {
                _categoryStats.value = StatsCategoryState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }
}