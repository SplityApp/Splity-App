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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import com.igorj.splity.model.main.stats.StatsState
import com.igorj.splity.ui.theme.localColorScheme
import com.igorj.splity.ui.theme.typography
import com.igorj.splity.util.LoadingController
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Payment(
    val date: LocalDate,
    val amount: Double
)

@Composable
fun StatsScreen(
    statsViewModel: StatsViewModel = koinViewModel()
) {
    val initialStartDate: LocalDate = LocalDate.now().minusMonths(6)
    val initialEndDate: LocalDate = LocalDate.now()

    val onStartDateChange: (LocalDate) -> Unit = { startDate ->
        statsViewModel.getStats(startDate.toString(), initialEndDate.toString())
    }
    val onEndDateChange: (LocalDate) -> Unit = { endDate ->
        statsViewModel.getStats(initialStartDate.toString(), endDate.toString())
    }

    LaunchedEffect(true) {
        statsViewModel.getStats(initialStartDate.toString(), initialEndDate.toString())
    }

    val statsScreenState by statsViewModel.stats.collectAsStateWithLifecycle()

    var startDate by remember { mutableStateOf(initialStartDate) }
    var endDate by remember { mutableStateOf(initialEndDate) }
    var isStartDatePickerVisible by remember { mutableStateOf(false) }
    var isEndDatePickerVisible by remember { mutableStateOf(false) }

    when(val state = statsScreenState) {
        StatsState.Loading -> {
            LaunchedEffect(true) {
                LoadingController.showLoading()
            }

            DisposableEffect(true) {
                onDispose {
                    LoadingController.hideLoading()
                }
            }
        }
        is StatsState.Success -> {
            parseToDate("2024/1")
            val payments = state.stats.map { Payment(parseToDate(it.date), it.totalAmount) }

            val filteredPayments = remember(payments, startDate, endDate) {
                payments.filter { it.date.isAfter(startDate.minusDays(1)) && it.date.isBefore(endDate.plusDays(1)) }
            }

            val monthsInRange = startDate.monthValue..endDate.monthValue
            val groupedPayments = monthsInRange.associateWith { month ->
                filteredPayments.filter { it.date.monthValue == month }.sumOf { it.amount }
            }

            val months = (monthsInRange.first - 1..monthsInRange.last + 1).toList()
            val monthLabels = months.map { monthValue ->
                if (monthValue == monthsInRange.first - 1 || monthValue == monthsInRange.last + 1) {
                    " "
                } else {
                    LocalDate.of(2024, monthValue, 1).month.name.substring(0, 3)
                }
            }

            val amounts = months.map { groupedPayments[it] ?: 0.0 }

            val maxAmount = (amounts.maxOrNull() ?: 0.0) * 1.2

            val barChartData = BarChartData(
                chartData = months.mapIndexed { index, _ ->
                    BarData(
                        point = Point(x = index.toFloat(), y = amounts[index].toFloat()),
                        label = "$${amounts[index]}",
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                xAxisData = AxisData.Builder()
                    .axisStepSize(10.dp)
                    .steps(months.size)
                    .bottomPadding(40.dp)
                    .labelData { index -> monthLabels[index] }
                    .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .backgroundColor(MaterialTheme.colorScheme.background)
                    .build(),
                yAxisData = AxisData.Builder()
                    .steps(5)
                    .labelAndAxisLinePadding(40.dp)
                    .labelData { index -> ((index * maxAmount / 5).toInt()).toString() }
                    .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
                    .backgroundColor(MaterialTheme.colorScheme.background)
                    .build()
            )

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DatePicker(
                        date = startDate,
                        isPickerVisible = isStartDatePickerVisible,
                        onPickerVisibilityChange = { isStartDatePickerVisible = it }
                    )
                    DatePicker(
                        date = endDate,
                        isPickerVisible = isEndDatePickerVisible,
                        onPickerVisibilityChange = { isEndDatePickerVisible = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    barChartData = barChartData
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Monthly Payments",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (isStartDatePickerVisible) {
                DatePickerDialog(
                    date = startDate,
                    onDateChange = {
                        startDate = it
                        onStartDateChange(it)
                        isStartDatePickerVisible = false
                    },
                    onDismissRequest = { isStartDatePickerVisible = false }
                )
            }

            if (isEndDatePickerVisible) {
                DatePickerDialog(
                    date = endDate,
                    onDateChange = {
                        endDate = it
                        onEndDateChange(it)
                        isEndDatePickerVisible = false
                    },
                    onDismissRequest = { isEndDatePickerVisible = false }
                )
            }
        }
        is StatsState.Error -> {
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

fun parseToDate(input: String): LocalDate {
    // Define a formatter for the full date (year, month, and day)
    val formatter = DateTimeFormatter.ofPattern("yyyy/M/d")

    // Append a default day to the input string and parse it
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
            text = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
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
    val day = date.dayOfMonth

    var selectedYear by remember { mutableStateOf(year) }
    var selectedMonth by remember { mutableStateOf(month) }
    var selectedDay by remember { mutableStateOf(day) }

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
                    onDateChange(LocalDate.of(selectedYear, selectedMonth, selectedDay))
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
