package com.mrx.recipegenerator.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mrx.recipegenerator.core.domain.usecase.AddHistoryUseCase
import com.mrx.recipegenerator.util.CommonUtil.getBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val generativeModel: GenerativeModel, private val addHistoryUseCase: AddHistoryUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text("$prompt Recipe")
                    }
                )
                response.text?.let { outputContent ->
                    addHistory(prompt, outputContent, null)
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun sendPromptedImage(
        context: Context,
        imageUri: Uri?,
    ) {
        _uiState.value = UiState.Loading
        val prompt = "Recipe based on the image above"
        val bitmap = getBitmapFromUri(
            context,
            imageUri
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap!!)
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    addHistory(prompt, outputContent, imageUri)
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    private fun addHistory(prompt: String, output: String, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            addHistoryUseCase(prompt, output, imageUri)
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Initial
    }
}