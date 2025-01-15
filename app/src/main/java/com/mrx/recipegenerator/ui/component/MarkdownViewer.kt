package com.mrx.recipegenerator.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mrx.recipegenerator.util.CommonUtil.parseMarkdown

@Composable
fun MarkdownViewer(markdownText: String, modifier: Modifier = Modifier) {
    val parsedContent = parseMarkdown(markdownText)

    Column(modifier = modifier) {
        parsedContent.forEach { component ->
            component()
        }
    }
}