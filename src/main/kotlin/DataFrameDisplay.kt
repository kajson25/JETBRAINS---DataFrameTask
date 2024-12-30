@file:Suppress("ktlint:standard:no-wildcard-imports")

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toMap
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

@Composable
fun HierarchicalCardView(dataFrame: DataFrame<*>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(dataFrame.rows().count()) { rowIndex ->
            val row = dataFrame[rowIndex].toMap()
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                elevation = 4.dp,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    row.forEach { (key, value) ->
                        RenderDynamicData(key, value, Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SchemaView(dataFrame: DataFrame<*>) {
    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        Text("Schema:", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))
        dataFrame.schema().columns.forEach { column ->
            Text("- ${column.key}: ${column.value.type}")
        }
    }
}

@Composable
fun RenderDynamicData(
    key: String,
    value: Any?,
    modifier: Modifier = Modifier,
) {
    if (value == null) {
        Text(text = "$key: null", modifier = modifier, color = MaterialTheme.colors.error)
        return
    }

    when (value) {
        is String -> {
            if (value.startsWith("http") || value.startsWith("/")) {
                RenderImage(key, value, modifier)
                return
            }
            val maxLength = 50
            if (value.length > maxLength) {
                var expanded by remember { mutableStateOf(false) }
                Text(
                    text = if (expanded) "$key: $value" else "$key: ${value.take(maxLength)}...",
                    modifier = modifier.clickable { expanded = !expanded },
                    color = MaterialTheme.colors.primary,
                )
            } else {
                Text(text = "$key: $value", modifier = modifier)
            }
        }
        is Boolean -> {
            Text(text = "$key: ${if (value) "Yes" else "No"}", modifier = modifier)
        }

        is Number -> {
            Text(text = "$key: $value", modifier = modifier)
        }

        is Map<*, *> -> {
            var expanded by remember { mutableStateOf(false) }
            ClickableText(
                text = buildAnnotatedString { append(if (expanded) "▼ $key" else "▶ $key") },
                onClick = { expanded = !expanded },
                modifier = modifier,
            )
            if (expanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    value.forEach { (nestedKey, nestedValue) ->
                        RenderDynamicData(nestedKey.toString(), nestedValue, Modifier.padding(4.dp))
                    }
                }
            }
        }

        else -> {
            Text(text = "$key: $value", modifier = modifier)
        }
    }
}

@Composable
fun RenderImage(
    key: String,
    pathOrUrl: String,
    modifier: Modifier = Modifier,
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(pathOrUrl) {
        imageBitmap = loadImageFromPathOrUrl(pathOrUrl)
    }

    Column(modifier = modifier) {
        Text(text = "$key:", modifier = Modifier.padding(bottom = 4.dp))
        if (imageBitmap != null) {
            Image(bitmap = imageBitmap!!, contentDescription = key, modifier = Modifier.size(100.dp))
        } else {
            Text(text = "Loading image...", modifier = Modifier.padding(8.dp))
        }
    }
}

suspend fun loadImageFromPathOrUrl(pathOrUrl: String): ImageBitmap? =
    withContext(Dispatchers.IO) {
        try {
            val image =
                when {
                    pathOrUrl.startsWith("http") -> {
                        // Load from URL
                        ImageIO.read(URL(pathOrUrl))
                    }

                    pathOrUrl.startsWith("/") -> {
                        // Load from resources
                        val resourceStream = {}.javaClass.getResourceAsStream(pathOrUrl)
                        resourceStream?.let { ImageIO.read(it) }
                    }

                    else -> {
                        // Load from file system
                        ImageIO.read(File(pathOrUrl))
                    }
                }
            image?.toComposeImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
