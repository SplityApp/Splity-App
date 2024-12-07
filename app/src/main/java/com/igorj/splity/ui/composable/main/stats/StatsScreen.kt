package com.igorj.splity.ui.composable.main.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.igorj.splity.model.main.stats.StatsCategoryState
import com.igorj.splity.model.main.stats.StatsMonthlyState
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import com.igorj.splity.util.LoadingController
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StatsScreen(
    statsViewModel: StatsViewModel = koinViewModel()
) {
    val initialStartDate: LocalDate = LocalDate.now().minusMonths(6)
    val initialEndDate: LocalDate = LocalDate.now()

    var startDateMonthly by remember { mutableStateOf(initialStartDate) }
    var endDateMonthly by remember { mutableStateOf(initialEndDate) }
    var isStartDatePickerVisibleMonthly by remember { mutableStateOf(false) }
    var isEndDatePickerVisibleMonthly by remember { mutableStateOf(false) }

    var startDateCategory by remember { mutableStateOf(initialStartDate) }
    var endDateCategory by remember { mutableStateOf(initialEndDate) }
    var isStartDatePickerVisibleCategory by remember { mutableStateOf(false) }
    var isEndDatePickerVisibleCategory by remember { mutableStateOf(false) }

    val statsScreenState by statsViewModel.monthlyStats.collectAsStateWithLifecycle()
    val categoryStatsState by statsViewModel.categoryStats.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString())
        statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString())
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Monthly Expenses",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DatePicker(
                date = startDateMonthly,
                isPickerVisible = isStartDatePickerVisibleMonthly,
                onPickerVisibilityChange = { isStartDatePickerVisibleMonthly = it }
            )
            DatePicker(
                date = endDateMonthly,
                isPickerVisible = isEndDatePickerVisibleMonthly,
                onPickerVisibilityChange = { isEndDatePickerVisibleMonthly = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = statsScreenState) {
            StatsMonthlyState.Loading -> {
                LaunchedEffect(true) {
                    LoadingController.showLoading()
                }

                DisposableEffect(true) {
                    onDispose {
                        LoadingController.hideLoading()
                    }
                }
            }
            is StatsMonthlyState.Success -> {
                val payments = state.stats.map { Payment(parseToDate(it.date), it.totalAmount) }
                MonthlyExpensesChart(payments = payments, startDate = startDateMonthly, endDate = endDateMonthly)
            }
            is StatsMonthlyState.Error -> {
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Category Expenses",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DatePicker(
                date = startDateCategory,
                isPickerVisible = isStartDatePickerVisibleCategory,
                onPickerVisibilityChange = { isStartDatePickerVisibleCategory = it }
            )
            DatePicker(
                date = endDateCategory,
                isPickerVisible = isEndDatePickerVisibleCategory,
                onPickerVisibilityChange = { isEndDatePickerVisibleCategory = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = categoryStatsState) {
            StatsCategoryState.Loading -> {
                LaunchedEffect(true) {
                    LoadingController.showLoading()
                }

                DisposableEffect(true) {
                    onDispose {
                        LoadingController.hideLoading()
                    }
                }
            }
            is StatsCategoryState.Success -> {
                CategoryExpensesChart(stats = state.stats)
            }
            is StatsCategoryState.Error -> {
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

    if (isStartDatePickerVisibleMonthly) {
        DatePickerDialog(
            date = startDateMonthly,
            onDateChange = {
                startDateMonthly = it
                statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString())
                isStartDatePickerVisibleMonthly = false
            },
            onDismissRequest = { isStartDatePickerVisibleMonthly = false }
        )
    }

    if (isEndDatePickerVisibleMonthly) {
        DatePickerDialog(
            date = endDateMonthly,
            onDateChange = {
                endDateMonthly = it
                statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString())
                isEndDatePickerVisibleMonthly = false
            },
            onDismissRequest = { isEndDatePickerVisibleMonthly = false }
        )
    }

    if (isStartDatePickerVisibleCategory) {
        DatePickerDialog(
            date = startDateCategory,
            onDateChange = {
                startDateCategory = it
                statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString())
                isStartDatePickerVisibleCategory = false
            },
            onDismissRequest = { isStartDatePickerVisibleCategory = false }
        )
    }

    if (isEndDatePickerVisibleCategory) {
        DatePickerDialog(
            date = endDateCategory,
            onDateChange = {
                endDateCategory = it
                statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString())
                isEndDatePickerVisibleCategory = false
            },
            onDismissRequest = { isEndDatePickerVisibleCategory = false }
        )
    }
}


fun parseToDate(input: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d")
    return LocalDate.parse("$input/1", formatter)
}

@Composable
fun DatePicker(
    date: LocalDate,
    isPickerVisible: Boolean,
    onPickerVisibilityChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onPickerVisibilityChange(!isPickerVisible)
        }
    ) {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("MMM, yyyy")),
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Date Picker",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun DatePickerDialog(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit
) {
    val year = date.year
    val month = date.monthValue

    var selectedYear by remember { mutableStateOf(year) }
    var selectedMonth by remember { mutableStateOf(month) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Select a date")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NumberPicker(
                        title = "Year",
                        value = selectedYear,
                        onValueChange = { selectedYear = it },
                        range = 1980..2030
                    )
                    NumberPicker(
                        title = "Month",
                        value = selectedMonth,
                        onValueChange = { selectedMonth = it },
                        range = 1..12
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDateChange(LocalDate.of(selectedYear, selectedMonth, 1))
                }
            ) {
                Text(
                    "OK",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@Composable
fun NumberPicker(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onValueChange(value + 1) },
                enabled = value < range.last
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Increase value"
                )
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = { onValueChange(value - 1) },
                enabled = value > range.first
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Decrease value"
                )
            }
        }
    }
}
