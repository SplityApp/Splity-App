package com.igorj.splity.ui.composable.main.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import java.time.LocalDate

data class Payment(
    val date: LocalDate,
    val amount: Double
)

@Composable
fun MonthlyExpensesChart(
    payments: List<Payment>,
    startDate: LocalDate,
    endDate: LocalDate,
    currency: String?
) {
    val filteredPayments = payments.filter { it.date.isAfter(startDate.minusDays(1)) && it.date.isBefore(endDate.plusDays(1)) }
    val (monthsInRange, monthLabels) = calculateMonthsInRange(startDate, endDate)

    val monthsWithPadding = listOf(monthsInRange.first() - 1) + monthsInRange + listOf(monthsInRange.last() + 1)
    val monthLabelsWithPadding = listOf(" ") + monthLabels + listOf(" ")

    val groupedPayments = monthsWithPadding.associateWith { month ->
        filteredPayments.filter { it.date.monthValue == month }.sumOf { it.amount }
    }

    val amounts = monthsWithPadding.map { groupedPayments[it] ?: 0.0 }
    val maxAmount = (amounts.maxOrNull() ?: 0.0) * 1.2

    val barChartData = BarChartData(
        chartData = monthsWithPadding.mapIndexed { index, _ ->
            BarData(
                point = Point(x = index.toFloat(), y = amounts[index].toFloat()),
                label = if (index == 0 || index == amounts.lastIndex) "" else "${currency ?: ""}${amounts[index]}",
                color = MaterialTheme.colorScheme.primary
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        paddingEnd = 0.dp,
        xAxisData = AxisData.Builder()
            .axisStepSize(10.dp)
            .steps(monthsWithPadding.size)
            .bottomPadding(40.dp)
            .labelData { index -> monthLabelsWithPadding[index] }
            .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .backgroundColor(MaterialTheme.colorScheme.background)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(5)
            .labelAndAxisLinePadding(40.dp)
            .labelData { index -> "${currency ?: ""} ${(index * maxAmount / 5).toInt()}" }
            .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .backgroundColor(MaterialTheme.colorScheme.background)
            .build()
    )

    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        barChartData = barChartData
    )
}

fun calculateMonthsInRange(startDate: LocalDate, endDate: LocalDate): Pair<List<Int>, List<String>> {
    val months = generateSequence(startDate) { it.plusMonths(1) }
        .takeWhile { it <= endDate }
        .map { it.monthValue }
        .toList()

    val monthsWithPadding = listOf(months.first() - 1) + months + listOf(months.last() + 1)
    val labels = monthsWithPadding.map { monthValue ->
        if (monthValue < 1 || monthValue > 12) " "
        else LocalDate.of(2024, monthValue, 1).month.name.substring(0, 3)
    }

    return monthsWithPadding to labels
}
