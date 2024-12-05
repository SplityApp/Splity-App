package com.igorj.splity.model.main.profile

import com.igorj.splity.model.main.UserInfo

sealed class UserInfoState {
    data object Loading : UserInfoState()
    data class Success(val userInfo: UserInfo) : UserInfoState()
    data class Error(val message: String) : UserInfoState()
}
