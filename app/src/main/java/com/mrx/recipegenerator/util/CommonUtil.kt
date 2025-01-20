package com.mrx.recipegenerator.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.io.InputStream

object CommonUtil {
    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Recipe", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun getBitmapFromUri(context: Context, uri: Uri?): Bitmap? {
        if (uri != null) {
            var inputStream: InputStream? = null
            return try {
                inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                inputStream?.close()
            }
        } else {
            Toast.makeText(context, "Image cannot be empty", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    fun parseMarkdown(text: String, color: Color): List<@Composable () -> Unit> {
        val parsedComponents = mutableListOf<@Composable () -> Unit>()

        val lines = text.split("\n")

        lines.forEach { line ->
            when {
                line.startsWith("# ") -> {
                    parsedComponents.add {
                        Text(
                            text = line.removePrefix("# "),
                            color = color,
                            fontWeight = FontWeight.Bold,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 24.sp
                            )
                        )
                    }
                }

                line.contains("**") -> {
                    parsedComponents.add {
                        val content = line.substringAfter("**").substringBeforeLast("**")
                        Text(
                            text = content,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                line.contains("*") -> {
                    parsedComponents.add {
                        val content = line.substringAfter("*").substringBeforeLast("*")
                        Text(
                            text = content,
                            color = color,
                            style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        )
                    }
                }

                else -> {
                    parsedComponents.add {
                        Text(text = line, color = color)
                    }
                }
            }
        }

        return parsedComponents
    }
}