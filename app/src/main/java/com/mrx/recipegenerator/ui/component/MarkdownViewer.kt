package com.mrx.recipegenerator.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mrx.recipegenerator.util.CommonUtil.parseMarkdown

@Composable
fun MarkdownViewer(
    markdownText: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val parsedContent = parseMarkdown(markdownText, color)

    Column(modifier = modifier) {
        parsedContent.forEach { component ->
            component()
        }
    }
}