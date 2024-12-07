package com.igorj.splity.ui.composable.main.stats

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    endDate: LocalDate
) {
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
                color = MaterialTheme.colorScheme.primary,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        xAxisData = AxisData.Builder()
            .axisStepSize(10.dp)
            .steps(months.size)
            .labelData { index -> monthLabels[index] }
            .axisLineColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .axisLabelColor(MaterialTheme.colorScheme.onSurfaceVariant)
            .backgroundColor(MaterialTheme.colorScheme.background)
            .build(),
        yAxisData = AxisData.Builder()
            .steps(5)
            .labelData { index -> ((index * maxAmount / 5).toInt()).toString() }
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
