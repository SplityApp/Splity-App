package com.igorj.splity.ui.composable.main.groupDetails

import ScreenSwitch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.igorj.splity.ui.composable.main.groupDetails.balance.BalancesScreen
import com.igorj.splity.ui.composable.main.groupDetails.expense.ExpensesScreen
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography

@Composable
fun GroupDetailsScreen(
    groupId: String,
    groupName: String
) {
    Column {
        Text(
            text = groupName,
            style = typography.headlineLarge,
            color = localColorScheme.secondary,
            modifier = Modifier.padding(16.dp)
        )

        ScreenSwitch(
            leftScreen = {
                ExpensesScreen(groupId = groupId)
            },
            rightScreen = {
                BalancesScreen(groupId = groupId)
            },
            leftLabel = "Expenses",
            rightLabel = "Balances",
            leftIcon = Icons.Default.AttachMoney,
            rightIcon = Icons.Default.Balance
        )
    }
}