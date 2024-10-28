package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.DarkGreen
import com.igorj.splity.ui.theme.DarkGrey
import com.igorj.splity.ui.theme.LightGrey
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun HomeScreen(
    onGoToProfileAction: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(localColorScheme.background),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column {
            Text(
                text = "Current expenses",
                style = typography.headlineMedium,
                color = localColorScheme.secondary,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                items(1) {
                    HomeCard(
                        title = "Japan weekend",
                        amount = 0.0,
                        currency = "USD"
                    )
                    HomeCard(
                        title = "Beer night",
                        amount = -100.0,
                        currency = "USD"
                    )
                    HomeCard(
                        title = "Shots",
                        amount = 100.0,
                        currency = "USD"
                    )
                }
            }

            Button(
                onClick = {
                    onGoToProfileAction()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGrey
                )
            ) {
                Text(
                    text = "Go to profile",
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
