package com.igorj.splity

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.ChangeNotificationsRequest
import com.igorj.splity.api.ProfileApi
import com.igorj.splity.model.main.profile.UserInfoState
import com.igorj.splity.repository.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileApi: ProfileApi,
    private val userInfoRepository: UserInfoRepository
): ViewModel() {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Loading)
    val userInfoState = _userInfoState.asStateFlow()

    private val _isUpdatingPreferences = MutableStateFlow(false)
    val isUpdatingPreferences = _isUpdatingPreferences.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            try {
                userInfoRepository.getUserInfo()?.let { cachedUserInfo ->
                    _userInfoState.value = UserInfoState.Success(cachedUserInfo)
                    return@launch
                }

                val response = profileApi.getUserInfo()
                if (response.isSuccessful) {
                    response.body()?.let { userInfo ->
                        userInfoRepository.saveUserInfo(userInfo)
                        _userInfoState.value = UserInfoState.Success(userInfo)
                    } ?: run {
                        _userInfoState.value = UserInfoState.Error("Empty response")
                    }
                } else {
                    _userInfoState.value = UserInfoState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _userInfoState.value = UserInfoState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refreshUserInfo() {
        _userInfoState.value = UserInfoState.Loading
        viewModelScope.launch {
            try {
                val response = profileApi.getUserInfo()
                if (response.isSuccessful) {
                    response.body()?.let { userInfo ->
                        userInfoRepository.saveUserInfo(userInfo)
                        _userInfoState.value = UserInfoState.Success(userInfo)
                    } ?: run {
                        _userInfoState.value = UserInfoState.Error("Empty response")
                    }
                } else {
                    _userInfoState.value = UserInfoState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _userInfoState.value = UserInfoState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateNotificationPreferences(context: Context, allowed: Boolean) {
        viewModelScope.launch {
            try {
                _isUpdatingPreferences.value = true

                val response = profileApi.updateNotificationPreferences(
                    ChangeNotificationsRequest(allowed)
                )

                if (response.isSuccessful) {
                    response.body()?.let { notificationsResponse ->
                        val currentState = _userInfoState.value
                        if (currentState is UserInfoState.Success) {
                            val updatedUserInfo = currentState.userInfo.copy(
                                allowedNotifications = notificationsResponse.allowedNotifications
                            )
                            userInfoRepository.saveUserInfo(updatedUserInfo)
                            _userInfoState.value = UserInfoState.Success(updatedUserInfo)
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to update preferences", Toast.LENGTH_SHORT).show()
                    loadUserInfo()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show()
                loadUserInfo()
            } finally {
                _isUpdatingPreferences.value = false
            }
        }
    }

}