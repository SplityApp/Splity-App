package com.igorj.splity.ui.composable.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun AuthBottomOptions(
    modifier: Modifier = Modifier,
    confirmText: String,
    alternativeText: String,
    alternativeHighlightText: String,
    onConfirmClicked: () -> Unit = {},
    onAlternativeClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val targetWidth = (maxWidth * 0.6f).coerceAtMost(250.dp)

            Button(
                modifier = Modifier
                    .height(60.dp)
                    .width(targetWidth),
                colors = ButtonDefaults.buttonColors(
                    containerColor = localColorScheme.primary,
                ),
                onClick = {
                    onConfirmClicked()
                }
            ) {
                Text(
                    text = confirmText,
                    style = typography.bodyLarge,
                    color = localColorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        val annotatedText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = localColorScheme.secondary)
            ) {
                append("$alternativeText ")
            }
            withStyle(
                style = SpanStyle(color = localColorScheme.primary).copy(
                    fontWeight = FontWeight.ExtraBold
                )
            ) {
                append(alternativeHighlightText)
            }
        }

        Text(
            modifier = Modifier.clickable {
                onAlternativeClicked()
            },
            text = annotatedText,
            style = typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun AuthBottomOptionsPreview() {
    AuthBottomOptions(
        confirmText = "Sign up",
        alternativeText = "Already have an account?",
        alternativeHighlightText = "Sign in"
    )
}
