package com.igorj.splity.ui.composable.main.groupDetails.expense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.model.main.expense.ExpenseState
import com.igorj.splity.model.main.groupDetails.GroupDetailsState
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpensesScreen(
    expenseViewModel: ExpenseViewModel = koinViewModel(),
    groupId: String,
    currency: String
) {
    LaunchedEffect(true) {
        expenseViewModel.getExpenses(groupId)
    }

    val expenseState by expenseViewModel.expenses.collectAsStateWithLifecycle()
    val isRefreshing by expenseViewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { expenseViewModel.getExpenses(groupId) })

    when (val state = expenseState) {
        ExpenseState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ExpenseState.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = localColorScheme.background,
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState),
                    ) {
                        val groupedExpenses = state.expenses
                            .groupBy { expense ->
                                val date = Date(expense.createdAt.time)
                                SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date)
                            }
                            .toSortedMap(reverseOrder())

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            groupedExpenses.forEach { (date, expensesForDate) ->
                                item {
                                    Text(
                                        text = date,
                                        style = typography.titleMedium,
                                        color = localColorScheme.secondary,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            top = 16.dp,
                                            bottom = 8.dp
                                        )
                                    )
                                }

                                items(expensesForDate) { expense ->
                                    ExpenseCard(
                                        expense = expense,
                                        currency = currency,
                                    )
                                }
                            }
                        }
                        PullRefreshIndicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            refreshing = isRefreshing,
                            state = pullRefreshState
                        )
                    }
                }
            )
        }
        is ExpenseState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = typography.headlineMedium,
                    color = localColorScheme.secondary
                )
            }
        }
    }
}