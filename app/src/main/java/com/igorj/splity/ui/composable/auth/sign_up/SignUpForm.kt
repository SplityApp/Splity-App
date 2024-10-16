package com.igorj.splity.ui.composable.auth.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igorj.splity.R
import com.igorj.splity.model.SignUpFormCallbacks
import com.igorj.splity.model.SignUpState
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    signUpState: SignUpState,
    signUpFormCallbacks: SignUpFormCallbacks
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signUpScreen_ui_topLabel),
            style = typography.headlineLarge,
            color = localColorScheme.secondary,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.3.sp
        )
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                label = stringResource(id = R.string.signUpScreen_ui_usernameTextFieldLabel),
                value = signUpState.username,
                onValueChange = {
                    signUpFormCallbacks.onUsernameChanged(it)
                },
            )
            TextField(
                label = stringResource(id = R.string.signUpScreen_ui_emailTextFieldLabel),
                value = signUpState.email,
                onValueChange = {
                    signUpFormCallbacks.onEmailChanged(it)
                },
            )
            TextField(
                label = stringResource(id = R.string.signUpScreen_ui_phoneNumberTextFieldLabel),
                value = signUpState.phoneNumber,
                onValueChange = {
                    signUpFormCallbacks.onPhoneNumberChanged(it)
                },
            )
            TextField(
                label = stringResource(id = R.string.signUpScreen_ui_passwordTextFieldLabel),
                value = signUpState.password,
                onValueChange = {
                    signUpFormCallbacks.onPasswordChanged(it)
                },
                type = TextFieldType.Password
            )
            TextField(
                label = stringResource(id = R.string.signUpScreen_ui_repeatPasswordTextFieldLabel),
                value = signUpState.repeatPassword,
                onValueChange = {
                    signUpFormCallbacks.onRepeatPasswordChanged(it)
                },
                type = TextFieldType.RepeatPassword
            )
        }
    }
}

@Preview
@Composable
private fun SignUpFormPreview() {
    SignUpForm(
        signUpState = SignUpState(),
        signUpFormCallbacks = SignUpFormCallbacks()
    )
}
