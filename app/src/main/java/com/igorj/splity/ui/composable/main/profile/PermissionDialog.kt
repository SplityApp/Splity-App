package com.igorj.splity.ui.composable.main.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        backgroundColor = localColorScheme.tertiaryContainer,
        onDismissRequest = onDismiss,
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    color = localColorScheme.secondary
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onGoToAppSettingsClick()
                        }
                        .padding(16.dp),
                    text = "OK",
                    style = typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = localColorScheme.secondary
                    )
                )
            }
        },
        title = {
            Text(
                text = "Permission required",
                style = typography.bodyLarge,
                color = localColorScheme.secondary
            )
        },
        text = {
            Text(
                text = "This app needs access to your notifications so that your friends can see when you paid them money.",
                style = typography.bodyMedium,
                color = localColorScheme.secondary
            )
        }
    )
}
