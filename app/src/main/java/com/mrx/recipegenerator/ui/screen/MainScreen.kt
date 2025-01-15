package com.mrx.recipegenerator.ui.screen

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.mrx.recipegenerator.ui.component.MarkdownViewer
import com.mrx.recipegenerator.util.CommonUtil.copyToClipboard
import com.mrx.recipegenerator.util.CommonUtil.getBitmapFromUri
import com.mrx.recipegenerator.viewmodel.MainViewModel
import com.mrx.recipegenerator.viewmodel.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val mainViewModel = koinViewModel<MainViewModel>()
    var prompt by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    var imageMode by rememberSaveable { mutableStateOf(false) }
    var selectedImages by rememberSaveable { mutableStateOf<Uri?>(Uri.EMPTY) }
    var cameraUri by rememberSaveable { mutableStateOf<Uri?>(Uri.EMPTY) }

    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImages = uri }
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) selectedImages = cameraUri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                cameraUri = context.contentResolver.insert(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    android.content.ContentValues()
                )
                cameraUri?.let {
                    cameraLauncher.launch(it)
                }
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    fun launchPhotoPicker() =
        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    fun launchCamera() {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraUri = context.contentResolver.insert(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                android.content.ContentValues()
            )
            cameraUri?.let {
                cameraLauncher.launch(it)
            }
        } else {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Recipe Generator") }) },
        floatingActionButton = {
            val isEnabled = prompt.isNotEmpty() || selectedImages != Uri.EMPTY
            FloatingActionButton(
                onClick = {
                    if (isEnabled) {
                        if (imageMode) {
                            mainViewModel.sendPromptedImage(
                                getBitmapFromUri(
                                    context,
                                    selectedImages
                                )
                            )
                        } else {
                            mainViewModel.sendPrompt(prompt)
                        }
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier.alpha(if (isEnabled) 1f else 0.5f)
            ) { Text("Generate!", modifier = Modifier.padding(16.dp)) }
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (!imageMode) {
                Button(
                    onClick = { imageMode = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Generate based on Image Instead")
                }
                OutlinedTextField(
                    value = prompt,
                    label = { Text("Recipe Prompt") },
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                Button(
                    onClick = { imageMode = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Generate based on Text Instead")
                }
                Row(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = { launchCamera() }, modifier = Modifier.weight(0.5f)) {
                        Text("Camera")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = { launchPhotoPicker() }, modifier = Modifier.weight(0.5f)) {
                        Text("Gallery")
                    }
                }
                AsyncImage(
                    model = selectedImages,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(150.dp),
                    contentScale = ContentScale.FillWidth
                )
            }

            if (uiState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                var textColor = MaterialTheme.colorScheme.onSurface
                if (uiState is UiState.Error) {
                    textColor = MaterialTheme.colorScheme.error
                    result = (uiState as UiState.Error).errorMessage
                } else if (uiState is UiState.Success) {
                    textColor = MaterialTheme.colorScheme.onSurface
                    result = (uiState as UiState.Success).outputText
                }
                val scrollState = rememberScrollState()
                Column {
                    if (result.isNotEmpty()) {
                        Button(
                            onClick = {
                                copyToClipboard(context, result)
                            },
                            Modifier.padding(start = 16.dp)
                        ) {
                            Text("Copy this Recipe!")
                        }
                    }
                    MarkdownViewer(
                        markdownText = result,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
