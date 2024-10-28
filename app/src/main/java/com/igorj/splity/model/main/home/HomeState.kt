package com.igorj.splity.model.main.home

import com.igorj.splity.model.main.UserGroup

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val userGroups: List<UserGroup>) : HomeState
    data class Error(val message: String) : HomeState
}
