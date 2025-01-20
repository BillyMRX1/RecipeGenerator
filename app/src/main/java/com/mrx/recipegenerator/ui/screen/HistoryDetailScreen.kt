package com.mrx.recipegenerator.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrx.recipegenerator.navigation.Screen
import com.mrx.recipegenerator.ui.component.MarkdownViewer
import com.mrx.recipegenerator.util.CommonUtil.copyToClipboard
import com.mrx.recipegenerator.viewmodel.HistoryDetailViewModel
import com.mrx.recipegenerator.viewmodel.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    navController: NavController,
    historyId: Int?
) {
    val viewModel = koinViewModel<HistoryDetailViewModel>()
    val uiState = viewModel.uiState.collectAsState().value
    val history = viewModel.history.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(historyId) {
        if (historyId != null) {
            viewModel.getHistoryById(historyId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text("Prompt History")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        history?.let {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Prompt: ${it.prompt}\n\nOutput: ${it.output}")
                                putExtra(Intent.EXTRA_SUBJECT, "Recipe from Recipe Generator")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        }
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share History")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    if (history != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            history.imageUri?.let { uriString ->
                                val uri = Uri.parse(uriString)
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "History Image",
                                    modifier = Modifier
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .padding(horizontal = 16.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Text(
                                text = "Prompt: ${history.prompt}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            )
                            MarkdownViewer(
                                markdownText = history.output,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Text("History not found.")
                    }
                }

                is UiState.Error -> {
                    Text(uiState.errorMessage)
                }

                else -> {
                    Text("No data loaded.")
                }
            }
        }
    }
}