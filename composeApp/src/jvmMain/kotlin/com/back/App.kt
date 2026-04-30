package com.back

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ViewMode { EditOnly, PreviewOnly, Split }

@Composable
fun App(text: MutableState<String>, fontSize: MutableState<Int>) {
    var viewMode by remember { mutableStateOf(ViewMode.Split) }
    var splitRatio by remember { mutableStateOf(0.5f) }

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
                val boxMaxWidth = maxWidth
                val totalWidthPx = with(LocalDensity.current) { boxMaxWidth.toPx() }
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
                                .width(boxMaxWidth * splitRatio)
                                .fillMaxHeight()
                        )
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outlineVariant)
                                .pointerInput(totalWidthPx) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val delta = dragAmount.x / totalWidthPx
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
