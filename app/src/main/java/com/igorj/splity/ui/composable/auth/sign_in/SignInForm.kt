package com.igorj.splity.ui.composable.auth.sign_in

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val (emailRef, passwordRef) = FocusRequester.createRefs()

    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signInScreen_ui_topLabel),
            style = typography.headlineLarge,
            color = localColorScheme.secondary,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.3.sp,
            modifier = Modifier.testTag(stringResource(
                id = R.string.signInForm_ui_topLabel_testTag)
            )
        )
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                modifier = Modifier
                    .focusRequester(emailRef)
                    .focusProperties {
                        next = passwordRef
                    },
                label = stringResource(id = R.string.signInScreen_ui_emailTextFieldLabel),
                value = email,
                onValueChange = {
                    onEmailChanged(it)
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
                    .focusRequester(passwordRef)
                    .focusProperties {
                        previous = emailRef
                    },
                label = stringResource(id = R.string.signInScreen_ui_passwordTextFieldLabel),
                value = password,
                onValueChange = {
                    onPasswordChanged(it)
                },
                type = TextFieldType.Password,
                onImeAction = {
                    focusManager.clearFocus()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                )
            )
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                val textFieldWidth = (maxWidth * 0.8f).coerceAtMost(400.dp)
                val forgotPasswordPaddingEnd = (maxWidth - textFieldWidth) / 2
                Text(
                    modifier = Modifier
                        .padding(end = forgotPasswordPaddingEnd)
                        .clickable {
                            onForgotPasswordClicked()
                        },
                    text = stringResource(id = R.string.signInScreen_ui_forgotPasswordTextButton),
                    style = typography.bodyMedium,
                    color = localColorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignInFormPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    SignInForm(
        email = email,
        password = password,
        onEmailChanged = { email = it },
        onPasswordChanged = { password = it }
    )
}
