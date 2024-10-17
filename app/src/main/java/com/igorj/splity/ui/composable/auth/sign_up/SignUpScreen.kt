package com.igorj.splity.ui.composable.auth.sign_up

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.R
import com.igorj.splity.model.AuthNavigationScreen
import com.igorj.splity.model.SignUpFormCallbacks
import com.igorj.splity.model.SignUpState
import com.igorj.splity.ui.composable.AuthBottomOptions
import com.igorj.splity.ui.composable.AuthTopBar
import com.igorj.splity.ui.theme.localColorScheme

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    var signUpState by remember {
        mutableStateOf(SignUpState())
    }

    Scaffold(
        modifier = modifier,
        containerColor = localColorScheme.background,
        topBar = {
            AuthTopBar()
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SignUpForm(
                    signUpState = signUpState,
                    signUpFormCallbacks = SignUpFormCallbacks(
                        onUsernameChanged = {  username ->
                            signUpState = signUpState.copy(username = username)
                        },
                        onEmailChanged = {  email ->
                            signUpState = signUpState.copy(email = email)
                        },
                        onPhoneNumberChanged = { phoneNumber ->
                            signUpState = signUpState.copy(phoneNumber = phoneNumber)
                        },
                        onPasswordChanged = { password ->
                            signUpState = signUpState.copy(password = password)
                        },
                        onRepeatPasswordChanged = { repeatPassword ->
                            signUpState = signUpState.copy(repeatPassword = repeatPassword)
                        },
                    )
                )
            }
        },
        bottomBar = {
            AuthBottomOptions(
                modifier = Modifier.padding(
                    vertical = 20.dp
                ),
                confirmText = stringResource(id = R.string.signUpScreen_ui_confirmButton),
                alternativeText = stringResource(id = R.string.signUpScreen_ui_bottomAlternativeText),
                alternativeHighlightText = stringResource(id = R.string.signUpScreen_ui_bottomAlternativeHighlightText),
                onConfirmClicked = {

                },
                onAlternativeClicked = {
                    onNavigate(AuthNavigationScreen.SignIn.name)
                }
            )
        }
    )
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen()
}
