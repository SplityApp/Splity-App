package com.igorj.splity.ui.composable.main.groupDetails.balance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.GetBalancesRequest
import com.igorj.splity.api.GroupApi
import com.igorj.splity.model.main.balance.BalanceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BalancesViewModel(
    private val groupApi: GroupApi
): ViewModel() {
    private val _balances = MutableStateFlow<BalanceState>(BalanceState.Loading)
    val balances = _balances

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getBalances(groupId: String) {
        _balances.value = BalanceState.Loading
        viewModelScope.launch {
            try {
                val response = groupApi.getBalances(GetBalancesRequest(groupId))
                Log.d("GroupDetailsViewModel", "getGroupDetails: $response")
                _balances.value = BalanceState.Success(response)
            } catch (e: Exception) {
                _balances.value = BalanceState.Error(e.message ?: "An error occurred")
                Log.d("GroupDetailsViewModel", "getGroupDetailsError: ${e.message}")
            }
        }
    }
}