package com.igorj.splity.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.R
import com.igorj.splity.model.TextFieldType
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    type: TextFieldType = TextFieldType.Common,
    onImeAction: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var isPasswordHintVisible by remember {
        mutableStateOf(false)
    }
    val isPassword = type == TextFieldType.Password
    val isTextHidden = type == TextFieldType.Password && !isPasswordHintVisible || type == TextFieldType.RepeatPassword

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val targetWidth = (maxWidth * 0.8f).coerceAtMost(400.dp)
        Row(
            modifier = modifier
                .height(58.dp)
                .width(targetWidth)
                .clip(RoundedCornerShape(24.dp))
                .background(localColorScheme.secondaryContainer)
                .padding(
                    start = 24.dp,
                    end = if (isPassword) 16.dp else 24.dp,
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = onValueChange,
                textStyle = typography.bodyMedium.copy(
                    color = localColorScheme.secondary,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = label,
                            style = typography.bodyMedium.copy(
                                color = localColorScheme.tertiary,
                            ),
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = keyboardOptions,
                cursorBrush = Brush.verticalGradient(
                    colors = listOf(
                        localColorScheme.secondary,
                        localColorScheme.secondary,
                    )
                ),
                visualTransformation = if (isTextHidden) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                keyboardActions = KeyboardActions(
                    onAny = {
                        onImeAction()
                    }
                )
            )
            if (isPassword) {
                val (icon, contentDescription) = if (isPasswordHintVisible) {
                    R.drawable.ic_eye to R.string.authScreen_alt_hidePasswordButton
                } else {
                    R.drawable.ic_closed_eye to R.string.authScreen_alt_showPasswordButton
                }

                IconButton(
                    onClick = {
                        isPasswordHintVisible = !isPasswordHintVisible
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = icon),
                        tint = localColorScheme.secondary,
                        contentDescription = stringResource(id = contentDescription)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TextFieldPreview() {
    var value by remember {
        mutableStateOf("")
    }
    
    TextField(
        label = "Email",
        value = value,
        onValueChange = {
            value = it
        },
        type = TextFieldType.Common
    )
}
