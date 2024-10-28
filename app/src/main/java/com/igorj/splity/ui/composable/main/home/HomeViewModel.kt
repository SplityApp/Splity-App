package com.igorj.splity.ui.composable.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.HomeApi
import com.igorj.splity.model.main.home.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
     private val homeApi: HomeApi
): ViewModel() {

    private val _userGroups: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val userGroups = _userGroups.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getUserGroups() {
         viewModelScope.launch {
            try {
                val response = homeApi.getUserGroups()
                _userGroups.value = HomeState.Success(response.groups)
            } catch (e: Exception) {
                _userGroups.value = HomeState.Error(e.message ?: "An error occurred")
            }
         }
    }
}
