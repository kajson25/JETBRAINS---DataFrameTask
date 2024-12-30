@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import helpers.calculateStatistics
import org.jetbrains.kotlinx.dataframe.DataFrame

@Composable
fun StatisticsView(dataFrame: DataFrame<*>) {
    val statistics = calculateStatistics(dataFrame)
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Summary Statistics:", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))
        statistics.forEach { (columnName, stats) ->
            println("$columnName: $stats")
            Text("$columnName:")
            stats.forEach { (statName, value) ->
                Text("  - $statName: $value")
            }
        }
    }
}

@Composable
fun Histogram(
    columnName: String,
    values: List<Double>,
) {
    val bins = 10
    val max = values.maxOrNull() ?: 1.0
    val min = values.minOrNull() ?: 0.0
    val range = max - min
    val binSize = range / bins
    val histogram = IntArray(bins)

    values.forEach { value ->
        val index = ((value - min) / binSize).toInt().coerceIn(0, bins - 1)
        histogram[index]++
    }

    Row(Modifier.fillMaxWidth().padding(8.dp)) {
        histogram.forEach { count ->
            Box(
                Modifier
                    .weight(1f)
                    .height((count * 10).dp)
                    .background(MaterialTheme.colors.primary),
            )
        }
    }
}
