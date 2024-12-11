package com.igorj.splity

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igorj.splity.api.ChangeUserInfoRequest
import com.igorj.splity.api.ProfileApi
import com.igorj.splity.model.main.profile.UserInfoState
import com.igorj.splity.repository.UserInfoRepository
import com.igorj.splity.util.SnackbarConfig
import com.igorj.splity.util.SnackbarController
import com.igorj.splity.util.SnackbarEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileApi: ProfileApi,
    private val userInfoRepository: UserInfoRepository
): ViewModel() {

    private val _userInfoState = MutableStateFlow<UserInfoState>(UserInfoState.Loading)
    val userInfoState = _userInfoState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeAt(0)
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
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

    fun changeUserInfo(
        username: String,
        email: String,
        charImage: String,
        emailHasChanged: Boolean,
        onComplete: () -> Unit,
    ){
        viewModelScope.launch {
            try {
                val response = profileApi.changeUserInfo(
                    ChangeUserInfoRequest(username, email, charImage)
                )

                if (response.isSuccessful) {
                    response.body()?.let { userInfo ->
                        val message = if (emailHasChanged) "Confirm change on email"
                            else "User info updated"
                        userInfoRepository.saveUserInfo(userInfo)
                        _userInfoState.value = UserInfoState.Success(userInfo)
                        SnackbarController.showSnackbar(
                            SnackbarEvent(
                                message = message,
                                config = SnackbarConfig(backgroundColor = Color.Green)
                            )
                        )
                    } ?: run {
                        SnackbarController.showSnackbar(
                            SnackbarEvent(
                                message = "Empty response",
                                config = SnackbarConfig(backgroundColor = Color.Red)
                            )
                        )
                    }
                } else {
                    SnackbarController.showSnackbar(
                        SnackbarEvent(
                            message = "Error: ${response.message()}",
                            config = SnackbarConfig(backgroundColor = Color.Red)
                        )
                    )
                }
            } catch (e: Exception) {
                SnackbarController.showSnackbar(
                    SnackbarEvent(
                        message = "Error: ${e.message}",
                        config = SnackbarConfig(backgroundColor = Color.Red)
                    )
                )
            } finally {
                onComplete()
            }
        }
    }

}