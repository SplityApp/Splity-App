package com.igorj.splity.ui.composable.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun ProfileScreen(
    onLogoutClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(localColorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                onLogoutClicked()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Red
            )
        ) {
            Text(
                text = "Logout",
                style = typography.headlineMedium,
                color = localColorScheme.secondary
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}