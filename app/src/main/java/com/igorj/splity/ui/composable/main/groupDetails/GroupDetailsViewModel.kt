package com.igorj.splity.ui.composable.main.groupDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.GetGroupDetailsRequest
import com.igorj.splity.api.GroupApi
import com.igorj.splity.model.main.groupDetails.GroupDetailsState
import com.igorj.splity.util.LoadingController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupDetailsViewModel(
    private val groupApi: GroupApi
): ViewModel() {
    private val _groupDetails = MutableStateFlow<GroupDetailsState>(GroupDetailsState.Loading)
    val groupDetails = _groupDetails

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun getGroupDetails(groupId: String) {
        viewModelScope.launch {
            LoadingController.showLoading()
            try {
                val response = groupApi.getGroupDetails(GetGroupDetailsRequest(groupId))
                Log.d("GroupDetailsViewModel", "getGroupDetails: $response")
                _groupDetails.value = GroupDetailsState.Success(response)
            } catch (e: Exception) {
                _groupDetails.value = GroupDetailsState.Error(e.message ?: "An error occurred")
                Log.d("GroupDetailsViewModel", "getGroupDetailsError: ${e.message}")
            }
            LoadingController.hideLoading()
        }
    }
}