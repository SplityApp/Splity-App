package com.igorj.splity.model.main.groupDetails

sealed interface GroupDetailsState {
    data object Loading : GroupDetailsState
    data class Success(val groupDetails: GroupDetails) : GroupDetailsState
    data class Error(val message: String) : GroupDetailsState
}