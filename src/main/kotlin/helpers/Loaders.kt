package helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.readExcel
import org.jetbrains.kotlinx.dataframe.io.readJson
import java.io.File

fun sampleDataFrame(): DataFrame<*> =
    dataFrameOf("Name", "Details")(
        "Alice",
        mapOf(
            "Age" to 25,
            "Address" to
                mapOf(
                    "City" to "New York",
                    "ZIP" to 10001,
                ),
            "Hobbies" to listOf("Reading", "Cycling"),
        ),
        "Bob",
        mapOf(
            "Age" to 30,
            "Address" to
                mapOf(
                    "City" to "San Francisco",
                    "ZIP" to 94105,
                ),
            "Hobbies" to listOf("Gaming", "Traveling"),
        ),
    )

suspend fun loadDataFrame(file: File): DataFrame<*> =
    withContext(Dispatchers.IO) {
        when (file.extension.lowercase()) {
            "csv" -> DataFrame.readCSV(file)
            "json" -> DataFrame.readJson(file)
            "xlsx", "xls" -> DataFrame.readExcel(file)
            "sql" -> DataFrame.read(file.readText())
            else -> throw IllegalArgumentException("Unsupported file type: ${file.extension}")
        }
    }
