package com.igorj.splity.ui.composable.main.groupDetails.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.GetExpensesRequest
import com.igorj.splity.api.GroupApi
import com.igorj.splity.model.main.expense.ExpenseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val groupApi: GroupApi
): ViewModel() {
    private val _expenses = MutableStateFlow<ExpenseState>(ExpenseState.Loading)
    val expenses = _expenses

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getExpenses(groupId: String) {
        viewModelScope.launch {
            try {
                val response = groupApi.getExpenses(GetExpensesRequest(groupId))
                _expenses.value = ExpenseState.Success(response)
            } catch (e: Exception) {
                _expenses.value = ExpenseState.Error(e.message ?: "An error occurred")
            }
        }
    }
}