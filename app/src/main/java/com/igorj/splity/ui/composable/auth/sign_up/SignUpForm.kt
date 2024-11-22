package com.igorj.splity.ui.composable.auth.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igorj.splity.R
import com.igorj.splity.model.auth.SignUpFormCallbacks
import com.igorj.splity.model.auth.SignUpState
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
    val focusManager = LocalFocusManager.current
    val (usernameRef, emailRef, phoneNumberRef, passwordRef, repeatPasswordRef) = FocusRequester.createRefs()

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
            letterSpacing = 0.3.sp,
            modifier = Modifier.testTag(stringResource(
                id = R.string.signUpForm_ui_topLabel_testTag)
            )
        )
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                modifier = Modifier
                    .focusRequester(usernameRef)
                    .focusProperties {
                        next = emailRef
                    },
                label = stringResource(id = R.string.signUpScreen_ui_usernameTextFieldLabel),
                value = signUpState.username,
                onValueChange = {
                    signUpFormCallbacks.onUsernameChanged(it)
                },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .focusRequester(emailRef)
                    .focusProperties {
                        previous = usernameRef
                        next = phoneNumberRef
                    },
                label = stringResource(id = R.string.signUpScreen_ui_emailTextFieldLabel),
                value = signUpState.email,
                onValueChange = {
                    signUpFormCallbacks.onEmailChanged(it)
                },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .focusRequester(phoneNumberRef)
                    .focusProperties {
                        previous = emailRef
                        next = passwordRef
                    },
                label = stringResource(id = R.string.signUpScreen_ui_phoneNumberTextFieldLabel),
                value = signUpState.phoneNumber,
                onValueChange = {
                    if (it.length < 10) {
                        signUpFormCallbacks.onPhoneNumberChanged(it)
                    }
                },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .focusRequester(passwordRef)
                    .focusProperties {
                        previous = phoneNumberRef
                        next = repeatPasswordRef
                    },
                label = stringResource(id = R.string.signUpScreen_ui_passwordTextFieldLabel),
                value = signUpState.password,
                onValueChange = {
                    signUpFormCallbacks.onPasswordChanged(it)
                },
                type = TextFieldType.Password,
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier
                    .focusRequester(repeatPasswordRef)
                    .focusProperties {
                        previous = passwordRef
                    },
                label = stringResource(id = R.string.signUpScreen_ui_repeatPasswordTextFieldLabel),
                value = signUpState.repeatPassword,
                onValueChange = {
                    signUpFormCallbacks.onRepeatPasswordChanged(it)
                },
                type = TextFieldType.RepeatPassword,
                onImeAction = {
                    focusManager.clearFocus()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                )
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
