package com.igorj.splity.ui.composable.main.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.StatsApi
import com.igorj.splity.api.StatsBetweenDatesRequest
import com.igorj.splity.model.main.stats.StatsState
import com.igorj.splity.util.LoadingController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val statsApi: StatsApi
): ViewModel() {
    private val _stats = MutableStateFlow<StatsState>(StatsState.Loading)
    val stats = _stats

    fun getStats(startDate: String, endDate: String) {
        viewModelScope.launch {
            LoadingController.showLoading()
            try {
                val response = statsApi.getStatsBetweenDates(StatsBetweenDatesRequest(startDate, endDate))
                _stats.value = StatsState.Success(response)
            } catch (e: Exception) {
                Log.e("StatsViewModel", "An error occurred", e)
                _stats.value = StatsState.Error(e.message ?: "An error occurred")
            } finally {
                LoadingController.hideLoading()
            }
        }
    }
}