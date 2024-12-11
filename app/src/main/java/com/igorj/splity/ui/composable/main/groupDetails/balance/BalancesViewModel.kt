package com.igorj.splity.ui.composable.main.groupDetails.balance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.FcmApi
import com.igorj.splity.api.GetBalancesRequest
import com.igorj.splity.api.GroupApi
import com.igorj.splity.api.ProcessPaymentApi
import com.igorj.splity.api.ProcessPaymentRequest
import com.igorj.splity.api.PushNotificationRequest
import com.igorj.splity.model.main.balance.BalanceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BalancesViewModel(
    private val groupApi: GroupApi,
    private val fcmApi: FcmApi,
    private val processPaymentApi: ProcessPaymentApi
): ViewModel() {
    private val _balances = MutableStateFlow<BalanceState>(BalanceState.Loading)
    val balances = _balances

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getBalances(groupId: String) {
        viewModelScope.launch {
            try {
                val response = groupApi.getBalances(GetBalancesRequest(groupId))
                _balances.value = BalanceState.Success(response)
            } catch (e: Exception) {
                _balances.value = BalanceState.Error(e.message ?: "An error occurred")
            }
        }
    }

    suspend fun processPayment(
        groupId: String,
        receiverId: String,
        amount: Double,
    ): Boolean {
        return try {
            val response = processPaymentApi.processPayment(
                ProcessPaymentRequest(
                    groupId = groupId,
                    receiverId = receiverId,
                    amount = amount
                )
            )

            if (response.isSuccessful) {
                getBalances(groupId)
                Log.d("BalancesViewModel", "Payment processed successfully")
                true
            } else {
                Log.e("BalancesViewModel", "Failed to process payment: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("BalancesViewModel", "Error processing payment", e)
            false
        }
    }

    fun sendPushNotification(
        userId: String,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            try {
                val pushNotificationRequest = PushNotificationRequest(
                    userId,
                    title,
                    description
                )
                Log.d("LOGCAT", pushNotificationRequest.toString())
                val response = fcmApi.pushNotification(pushNotificationRequest)
                if (response.isSuccessful) {
                    Log.d("BalancesViewModel", response.raw().toString())
                } else {
                    Log.e("BalancesViewModel", response.raw().toString())
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}