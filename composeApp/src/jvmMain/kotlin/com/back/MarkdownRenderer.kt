package com.back

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownRenderer(markdown: String, fontSize: Int = 16, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        markdown.lines().forEach { line ->
            if (line.isBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                MarkdownLine(line, fontSize)
            }
        }
    }
}

@Composable
private fun MarkdownLine(line: String, fontSize: Int) {
    when {
        line.startsWith("#### ") -> Text(parseInline(line.removePrefix("#### ")), style = TextStyle(fontSize = (fontSize + 2).sp, fontWeight = FontWeight.SemiBold))
        line.startsWith("### ")  -> Text(parseInline(line.removePrefix("### ")),  style = TextStyle(fontSize = (fontSize + 4).sp, fontWeight = FontWeight.SemiBold))
        line.startsWith("## ")   -> Text(parseInline(line.removePrefix("## ")),   style = TextStyle(fontSize = (fontSize + 6).sp, fontWeight = FontWeight.Bold))
        line.startsWith("# ")    -> Text(parseInline(line.removePrefix("# ")),    style = TextStyle(fontSize = (fontSize + 10).sp, fontWeight = FontWeight.Bold))
        else                     -> Text(parseInline(line), style = TextStyle(fontSize = fontSize.sp))
    }
}

private fun parseInline(text: String): AnnotatedString = buildAnnotatedString {
    val pattern = Regex("""\*\*(.+?)\*\*|\*(.+?)\*|==(.+?)==""")
    var lastIndex = 0
    pattern.findAll(text).forEach { match ->
        append(text.substring(lastIndex, match.range.first))
        when {
            match.groupValues[1].isNotEmpty() -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(match.groupValues[1])
                pop()
            }
            match.groupValues[2].isNotEmpty() -> {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                append(match.groupValues[2])
                pop()
            }
            match.groupValues[3].isNotEmpty() -> {
                pushStyle(SpanStyle(background = Color(0xFFFFFF00)))
                append(match.groupValues[3])
                pop()
            }
        }
        lastIndex = match.range.last + 1
    }
    append(text.substring(lastIndex))
}
