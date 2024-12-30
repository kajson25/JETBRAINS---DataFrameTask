package helpers

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.typeClass

fun calculateStatistics(dataFrame: DataFrame<*>): List<Pair<String, Map<String, Double>>> =
    dataFrame
        .columns()
        .filter { it.typeClass.java == Int::class.java } // Only numeric columns
        .map { column ->
            val numbers = column.values().filterIsInstance<Int>().map { it.toDouble() }
            val mean = numbers.average()
            val median =
                numbers.sorted().let { sorted ->
                    val middle = sorted.size / 2
                    if (sorted.size % 2 == 0) (sorted[middle - 1] + sorted[middle]) / 2 else sorted[middle]
                }
            val variance = numbers.map { it - mean }.sumOf { it * it } / numbers.size
            column.name() to mapOf("Mean" to mean, "Median" to median, "Variance" to variance)
        }
