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
import com.igorj.splity.model.main.expense.ExpenseCategory
import com.igorj.splity.model.main.stats.StatsBetweenDatesCategoryResponse

@Composable
fun CategoryExpensesChart(stats: List<StatsBetweenDatesCategoryResponse>) {
    val categories = ExpenseCategory.entries.toTypedArray()

    val categoryAmounts = categories.associateWith { category ->
        stats.firstOrNull { it.category == category.name }?.totalAmount ?: 0.0
    }

    val categoryLabels = listOf(" ") + categories.map { it.emoji } + listOf(" ")
    val amounts = listOf(0.0) + categories.map { categoryAmounts[it] ?: 0.0 } + listOf(0.0)

    val maxAmount = (amounts.maxOrNull() ?: 0.0) * 1.2

    val barChartData = BarChartData(
        chartData = amounts.mapIndexed { index, amount ->
            BarData(
                point = Point(x = index.toFloat(), y = amount.toFloat()),
                label = if (index == 0 || index == amounts.lastIndex) "" else "$${amount}",
                color = MaterialTheme.colorScheme.primary
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        xAxisData = AxisData.Builder()
            .axisStepSize(10.dp)
            .steps(categoryLabels.size)
            .bottomPadding(40.dp)
            .labelData { index -> categoryLabels[index] }
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

    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        barChartData = barChartData
    )
}
