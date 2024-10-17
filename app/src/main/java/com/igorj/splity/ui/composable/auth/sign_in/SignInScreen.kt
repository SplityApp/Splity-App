package com.igorj.splity.ui.composable.auth.sign_in

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
import com.igorj.splity.model.SignInState
import com.igorj.splity.ui.composable.AuthBottomOptions
import com.igorj.splity.ui.composable.AuthTopBar
import com.igorj.splity.ui.theme.localColorScheme

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    var signInState by remember {
        mutableStateOf(SignInState())
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
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                SignInForm(
                    email = signInState.email,
                    password = signInState.password,
                    onEmailChanged = {  email ->
                        signInState = signInState.copy(email = email)
                    },
                    onPasswordChanged = { password ->
                        signInState = signInState.copy(password = password)
                    },
                    onForgotPasswordClicked = {
                        onNavigate(AuthNavigationScreen.ForgotPassword.name)
                    }
                )
            }
        },
        bottomBar = {
            AuthBottomOptions(
                modifier = Modifier.padding(
                    vertical = 20.dp
                ),
                confirmText = stringResource(id = R.string.signInScreen_ui_confirmButton),
                alternativeText = stringResource(id = R.string.signInScreen_ui_bottomAlternativeText),
                alternativeHighlightText = stringResource(id = R.string.signInScreen_ui_bottomAlternativeHighlightText),
                onConfirmClicked = {

                },
                onAlternativeClicked = {
                    onNavigate(AuthNavigationScreen.SignUp.name)
                }
            )
        }
    )
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen()
}