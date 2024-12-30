@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import helpers.exportToHTML
import helpers.exportToPDF
import helpers.loadDataFrame
import helpers.sampleDataFrame
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser

fun main() =
    application {
        Window(onCloseRequest = ::exitApplication, title = "Data Viewer") {
            MaterialTheme {
                App()
            }
        }
    }

@Composable
fun App() {
    var dataFrame by remember { mutableStateOf(sampleDataFrame()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSchema by remember { mutableStateOf(false) }
    var showStatistics by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Viewer") },
                actions = {
                    Button(onClick = { showSchema = !showSchema }) {
                        Text(if (showSchema) "Hide Schema" else "Show Schema")
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = {
                            val file = chooseFile()
                            if (file != null) {
                                coroutineScope.launch {
                                    try {
                                        dataFrame = loadDataFrame(file)
                                        errorMessage = null
                                    } catch (e: Exception) {
                                        errorMessage = "Error loading file: ${e.message}"
                                    }
                                }
                            }
                        },
                    ) {
                        Text("Open File")
                    }

                    Button(
                        onClick = {
                            exportToHTML(dataFrame, "output.html")
                        },
                    ) {
                        Text("Export to HTML")
                    }

                    Button(
                        onClick = {
                            exportToPDF(dataFrame, "output.pdf")
                        },
                    ) {
                        Text("Export to PDF")
                    }

                    Button(
                        onClick = { showStatistics = !showStatistics }, // Toggle statistics view
                    ) {
                        Text(if (showStatistics) "Hide Analysis" else "Show Analysis")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp),
                ) {
                    when {
                        showSchema -> SchemaView(dataFrame)
                        showStatistics -> StatisticsView(dataFrame)
                        else -> HierarchicalCardView(dataFrame)
                    }
                }

                // Error message at the bottom
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        },
    )
}

fun chooseFile(): File? {
    val resourceDir = File("src/main/resources")
    val fileChooser = JFileChooser(resourceDir.takeIf { it.exists() })
    val result = fileChooser.showOpenDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        fileChooser.selectedFile
    } else {
        null
    }
}
