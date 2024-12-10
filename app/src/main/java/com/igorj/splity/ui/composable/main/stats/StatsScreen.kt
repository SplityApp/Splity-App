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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.igorj.splity.model.main.stats.StatsCurrenciesState
import com.igorj.splity.model.main.stats.StatsMonthlyState
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
    val currenciesState by statsViewModel.currencies.collectAsStateWithLifecycle()

    var selectedCurrency by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        statsViewModel.getCurrencies(initialStartDate.toString(), initialEndDate.toString())
    }

    LaunchedEffect(currenciesState) {
        if (currenciesState is StatsCurrenciesState.Success) {
            selectedCurrency = (currenciesState as StatsCurrenciesState.Success).currencies.firstOrNull()
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when(val state = currenciesState) {
            StatsCurrenciesState.Loading -> {
                LoadingController.showLoading()
                DisposableEffect(true) {
                    onDispose { LoadingController.hideLoading() }
                }
            }
            is StatsCurrenciesState.Success -> {
                val currencies = state.currencies
                if (selectedCurrency == null && currencies.isNotEmpty()) {
                    selectedCurrency = currencies.first()
                }

                Row {
                    Text(
                        text = "Selected Currency: ",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    DropdownSelector(
                        options = currencies,
                        selectedOption = selectedCurrency,
                        onOptionSelected = { currency ->
                            selectedCurrency = currency
                            statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString(), currency)
                            statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString(), currency)
                        }
                    )
                }

                when(val monthlyState = statsScreenState) {
                    StatsMonthlyState.Loading -> {
                        LoadingController.showLoading()
                        DisposableEffect(true) {
                            onDispose { LoadingController.hideLoading() }
                        }
                    }
                    is StatsMonthlyState.Success -> {
                        val payments = monthlyState.stats.map { Payment(parseToDate(it.date), it.totalAmount) }
                        Text(
                            text = "Monthly Expenses (${selectedCurrency ?: "Loading..."})",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(8.dp))

                        MonthlyExpensesChart(
                            payments = payments,
                            startDate = startDateMonthly,
                            endDate = endDateMonthly,
                            currency = selectedCurrency
                        )
                    }
                    is StatsMonthlyState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = monthlyState.message,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                when (val categoryState = categoryStatsState) {
                    StatsCategoryState.Loading -> {
                        LoadingController.showLoading()
                        DisposableEffect(true) {
                            onDispose { LoadingController.hideLoading() }
                        }
                    }
                    is StatsCategoryState.Success -> {
                        Text(
                            text = "Category Expenses (${selectedCurrency ?: "Loading..."})",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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

                        Spacer(modifier = Modifier.height(8.dp))

                        CategoryExpensesChart(
                            stats = categoryState.stats,
                            currency = selectedCurrency
                        )
                    }
                    is StatsCategoryState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = categoryState.message,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
            is StatsCurrenciesState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary
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
                statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString(), selectedCurrency ?: "")
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
                statsViewModel.getMonthlyStats(startDateMonthly.toString(), endDateMonthly.toString(), selectedCurrency ?: "")
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
                statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString(), selectedCurrency ?: "")
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
                statsViewModel.getCategoryStats(startDateCategory.toString(), endDateCategory.toString(), selectedCurrency ?: "")
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
fun DropdownSelector(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            text = selectedOption ?: "Select Currency",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { expanded = true }
                .padding(12.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        expanded = false
                        onOptionSelected(currency)
                    }
                )
            }
        }
    }
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
                    onDateChange(LocalDate.of(selectedYear, selectedMonth, LocalDate.now().dayOfMonth))
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
