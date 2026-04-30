package com.back

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.io.File

fun main() = application {
    val text = remember { mutableStateOf("") }
    val fontSize = remember { mutableStateOf(16) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "memoApp",
        onKeyEvent = { keyEvent ->
            when {
                keyEvent.type != KeyEventType.KeyDown -> false
                !keyEvent.isMetaPressed -> false
                keyEvent.key == Key.S -> { saveFile(text.value); true }
                keyEvent.key == Key.O -> {
                    val loaded = openFile()
                    if (loaded != null) text.value = loaded
                    true
                }
                keyEvent.key == Key.Equals -> { fontSize.value += 2; true }
                keyEvent.key == Key.Minus -> { fontSize.value = maxOf(8, fontSize.value - 2); true }
                keyEvent.key == Key.Zero -> { fontSize.value = 16; true }
                else -> false
            }
        }
    ) {
        App(text = text, fontSize = fontSize)
    }
}

private fun saveFile(text: String) {
    val dialog = FileDialog(null as java.awt.Frame?, "Save", FileDialog.SAVE)
    dialog.file = "memo.md"
    dialog.isVisible = true
    val dir = dialog.directory ?: return
    val file = dialog.file ?: return
    File(dir, file).writeText(text)
}

private fun openFile(): String? {
    val dialog = FileDialog(null as java.awt.Frame?, "Open", FileDialog.LOAD)
    dialog.isVisible = true
    val dir = dialog.directory ?: return null
    val file = dialog.file ?: return null
    return File(dir, file).readText()
}
