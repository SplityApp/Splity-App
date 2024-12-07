package com.igorj.splity.ui.composable.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.theme.DarkGreen
import com.igorj.splity.ui.theme.Green
import com.igorj.splity.ui.theme.LightGrey
import com.igorj.splity.ui.theme.Red
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCard(
    title: String,
    amount: Double,
    currency: String,
    onClick: () -> Unit
) {
    val amountColor: Color = when {
        amount > 0 -> Green
        amount == 0.0 -> LightGrey
        else -> Red
    }

    val isAmountAnInt = amount == amount.toInt().toDouble()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Start,
                style = typography.headlineMedium,
                color = localColorScheme.secondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${if (amount > 0) "+" else ""}${if (isAmountAnInt) amount.toInt() else "%.2f".format(amount)} $currency",
                textAlign = TextAlign.End,
                style = typography.headlineMedium,
                color = amountColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview
@Composable
fun HomeCardPreview() {
    HomeCard(
        title = "Japan weekend",
        amount = -25.54,
        currency = "zł",
        onClick = {}
    )
}