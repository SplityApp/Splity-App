package com.igorj.splity.ui.composable.main.groupDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.igorj.splity.model.main.expense.Expense
import com.igorj.splity.model.main.expense.ExpenseCategory
import com.igorj.splity.ui.theme.DarkGreen
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun ExpenseCard(
    expense: Expense,
    currency: String,
) {
    val isAmountAnInt = expense.amount == expense.amount.toInt().toDouble()
    val isValidCategory = ExpenseCategory.entries.any { it.title == expense.category }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkGreen
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = if (isValidCategory)
                    ExpenseCategory.valueOf(expense.category.uppercase()).emoji
                else
                    ExpenseCategory.UNKNOWN.emoji,
                style = typography.headlineLarge,
                color = localColorScheme.secondary
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = expense.description,
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary
                )

                Text(
                    text = "Paid by ${expense.paidBy}",
                    style = typography.bodyMedium,
                    color = localColorScheme.secondary
                )
            }

            Text(
                text = "${if (isAmountAnInt)  expense.amount.toInt() else "%.2f".format(expense.amount)} $currency",
                style = typography.headlineMedium,
                color = localColorScheme.secondary
            )
        }
    }
}

@Preview
@Composable
fun ExpenseCardPreview() {
    ExpenseCard(
        expense = Expense(
            id = "1",
            description = "Lunch",
            category = "Food",
            amount = 10.0,
            state = "Paid",
            paidBy = "John Doe",
            createdAt = java.sql.Timestamp(System.currentTimeMillis())
        ),
        currency = "USD"
    )
}