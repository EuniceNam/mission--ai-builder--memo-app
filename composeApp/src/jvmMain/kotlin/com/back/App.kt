package com.back

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import javax.swing.JFileChooser

enum class ViewMode { EditOnly, PreviewOnly, Split }

@Composable
fun App(text: MutableState<String>, fontSize: MutableState<Int>) {
    var viewMode by remember { mutableStateOf(ViewMode.Split) }
    var splitRatio by remember { mutableStateOf(0.5f) }
    var sidebarRatio by remember { mutableStateOf(0.2f) }
    var mdFiles by remember { mutableStateOf<List<File>>(emptyList()) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                listOf(
                    ViewMode.EditOnly to "편집만",
                    ViewMode.Split to "양쪽 보기",
                    ViewMode.PreviewOnly to "미리보기만"
                ).forEach { (mode, label) ->
                    Button(
                        onClick = { viewMode = mode },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewMode == mode)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(label)
                    }
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val outerMaxWidth = maxWidth
                val totalWidthPx = with(LocalDensity.current) { outerMaxWidth.toPx() }
                Row(modifier = Modifier.fillMaxSize()) {
                    Sidebar(
                        files = mdFiles,
                        onFileClick = { text.value = it.readText() },
                        onFolderSelect = {
                            val folder = chooseFolderDialog()
                            if (folder != null) {
                                mdFiles = folder.listFiles { f -> f.extension == "md" }
                                    ?.sortedBy { it.name } ?: emptyList()
                            }
                        },
                        modifier = Modifier.width(outerMaxWidth * sidebarRatio).fillMaxHeight()
                    )
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.outlineVariant)
                            .pointerInput(totalWidthPx) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    sidebarRatio = (sidebarRatio + dragAmount.x / totalWidthPx).coerceIn(0.1f, 0.4f)
                                }
                            }
                    )
                    BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        val contentMaxWidth = maxWidth
                        val contentWidthPx = with(LocalDensity.current) { contentMaxWidth.toPx() }
                        when (viewMode) {
                            ViewMode.EditOnly -> EditorPane(
                                text = text,
                                fontSize = fontSize,
                                modifier = Modifier.fillMaxSize()
                            )
                            ViewMode.PreviewOnly -> MarkdownRenderer(
                                markdown = text.value,
                                fontSize = fontSize.value,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            )
                            ViewMode.Split -> Row(modifier = Modifier.fillMaxSize()) {
                                EditorPane(
                                    text = text,
                                    fontSize = fontSize,
                                    modifier = Modifier
                                        .width(contentMaxWidth * splitRatio)
                                        .fillMaxHeight()
                                )
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                        .pointerInput(contentWidthPx) {
                                            detectDragGestures { change, dragAmount ->
                                                change.consume()
                                                val delta = dragAmount.x / contentWidthPx
                                                splitRatio = (splitRatio + delta).coerceIn(0.2f, 0.8f)
                                            }
                                        }
                                )
                                MarkdownRenderer(
                                    markdown = text.value,
                                    fontSize = fontSize.value,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .verticalScroll(rememberScrollState())
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Sidebar(
    files: List<File>,
    onFileClick: (File) -> Unit,
    onFolderSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
        Button(
            onClick = onFolderSelect,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Text("폴더 열기")
        }
        if (files.isEmpty()) {
            Text(
                text = "폴더를 선택하세요",
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(files) { file ->
                    Text(
                        text = file.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFileClick(file) }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

private fun chooseFolderDialog(): File? {
    val chooser = JFileChooser()
    chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
}

@Composable
private fun EditorPane(text: MutableState<String>, fontSize: MutableState<Int>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val minHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            BasicTextField(
                value = text.value,
                onValueChange = { text.value = it },
                textStyle = TextStyle(fontSize = fontSize.value.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = minHeight)
                    .padding(16.dp)
            )
        }
    }
}
