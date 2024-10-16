package com.igorj.splity.model

import androidx.compose.runtime.Stable

@Stable
class SignUpFormCallbacks(
    val onUsernameChanged: (String) -> Unit = {},
    val onEmailChanged: (String) -> Unit = {},
    val onPhoneNumberChanged: (String) -> Unit = {},
    val onPasswordChanged: (String) -> Unit = {},
    val onRepeatPasswordChanged: (String) -> Unit = {},
)
