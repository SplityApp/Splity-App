package com.igorj.splity.ui.composable.main.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.CreateGroupRequest
import com.igorj.splity.api.HomeApi
import com.igorj.splity.model.main.errorResponse
import com.igorj.splity.model.main.home.HomeState
import com.igorj.splity.util.SnackbarConfig
import com.igorj.splity.util.SnackbarController
import com.igorj.splity.util.SnackbarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Currency

class HomeViewModel(
     private val homeApi: HomeApi
): ViewModel() {

    private val _userGroups: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val userGroups = _userGroups.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getUserGroups() {
        viewModelScope.launch {
            val response = homeApi.getUserGroups()
            if (response.isSuccessful && response.body() != null) {
                _userGroups.value = HomeState.Success(response.body()!!.groups)
            } else {
                val error = errorResponse(response.errorBody()?.string())
                _userGroups.value = HomeState.Error(error.message)
            }
        }
    }

    fun createGroup(groupName: String, currency: Currency) {
        viewModelScope.launch {
            val response = homeApi.createGroup(
                CreateGroupRequest(
                    name = groupName,
                    currency = currency.currencyCode
                )
            )
            if (response.isSuccessful && response.body() != null) {
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = "Group created",
                        config = SnackbarConfig(backgroundColor = Color.Green)
                    )
                )
            } else {
                val error = errorResponse(response.errorBody()?.string())
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = error.message,
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
            }
        }
    }
}
