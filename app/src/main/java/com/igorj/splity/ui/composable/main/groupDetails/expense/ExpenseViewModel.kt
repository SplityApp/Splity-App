package com.igorj.splity.ui.composable.main.groupDetails.expense

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.AddExpenseRequest
import com.igorj.splity.api.ExpenseApi
import com.igorj.splity.api.GetExpensesRequest
import com.igorj.splity.api.GroupApi
import com.igorj.splity.model.main.expense.ExpenseEvent
import com.igorj.splity.model.main.expense.ExpenseState
import com.igorj.splity.util.LoadingController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val groupApi: GroupApi,
    private val expenseApi: ExpenseApi
): ViewModel() {

    private val _expenses = MutableStateFlow<ExpenseState>(ExpenseState.Loading)
    val expenses = _expenses

    private val _events = Channel<ExpenseEvent>()
    val events = _events.receiveAsFlow()

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

    fun addExpense(
        request: AddExpenseRequest
    ) {
        viewModelScope.launch {
            LoadingController.showLoading()
            try {
                val response = expenseApi.addExpense(request)
                if (response.isSuccessful) {
                    _events.send(ExpenseEvent.ExpenseAdded)
                    Log.d("ExpenseViewModel", "Expense added successfully")
                } else {
                    Log.e("ExpenseViewModel", "Failed to add expense: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Error adding expense", e)
            }
            LoadingController.hideLoading()
        }
    }
}