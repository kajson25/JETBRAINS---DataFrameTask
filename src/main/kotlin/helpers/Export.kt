package helpers

import com.itextpdf.html2pdf.HtmlConverter
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import java.io.File

fun exportToHTML(
    dataFrame: DataFrame<*>,
    filePath: String,
) {
    try {
        val htmlContent =
            buildString {
                append("<html><body>")
                append("<h1>DataFrame Visualization</h1>")
                append("<table border='1'>")

                // Add table headers
                append("<tr>")
                dataFrame.columns().forEach { column ->
                    append("<th>${column.name()}</th>")
                }
                append("</tr>")

                // Add rows
                dataFrame.rows().forEach { row ->
                    append("<tr>")
                    row.values().forEach { value ->
                        append("<td>${value ?: "null"}</td>")
                    }
                    append("</tr>")
                }

                append("</table>")
                append("</body></html>")
            }

        File(filePath).writeText(htmlContent)
        println("HTML export successful: $filePath")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Failed to export to HTML: ${e.message}")
    }
}

fun exportToPDF(
    dataFrame: DataFrame<*>,
    filePath: String,
) {
    try {
        val tempHtmlFile = File.createTempFile("dataframe", ".html")
        exportToHTML(dataFrame, tempHtmlFile.absolutePath)

        HtmlConverter.convertToPdf(tempHtmlFile, File(filePath))
        println("PDF export successful: $filePath")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Failed to export to PDF: ${e.message}")
    }
}
