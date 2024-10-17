package com.igorj.splity.ui.composable.auth.forgot_password

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igorj.splity.R
import com.igorj.splity.ui.composable.TextField
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun ForgotPasswordForm(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.6f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.forgotPasswordScreen_ui_topLabel),
                style = typography.headlineLarge,
                color = localColorScheme.secondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.3.sp
            )
            Text(
                text = stringResource(id = R.string.forgotPasswordScreen_ui_descriptionLabel),
                style = typography.bodySmall,
                color = localColorScheme.secondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.3.sp
            )
        }
        TextField(
            label = stringResource(id = R.string.signInScreen_ui_emailTextFieldLabel),
            value = email,
            onValueChange = {
                onEmailChanged(it)
            },
            onImeAction = {
                focusManager.clearFocus()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            )
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordFormPreview() {
    var email by remember { mutableStateOf("") }

    ForgotPasswordForm(
        email = email,
        onEmailChanged = { email = it }
    )
}
