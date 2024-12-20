package com.igorj.splity.model.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun NotificationsPreferencesSettingRow(
    modifier: Modifier = Modifier,
    areNotificationsAllowed: Boolean = false,
    onNotificationsAllowedChanged: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(localColorScheme.background)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Notifications",
            modifier = Modifier.weight(1f),
            style = typography.headlineSmall.copy(
                color = localColorScheme.secondary
            )
        )
        Switch(
            checked = areNotificationsAllowed,
            onCheckedChange = onNotificationsAllowedChanged,
        )
    }
}

@Preview
@Composable
private fun NotificationsPreferencesSettingRowPreview() {
    NotificationsPreferencesSettingRow()
}
