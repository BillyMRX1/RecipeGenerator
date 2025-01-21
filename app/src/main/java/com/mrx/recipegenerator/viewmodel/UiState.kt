package com.mrx.recipegenerator.viewmodel

sealed interface UiState {
    object Initial : UiState
    object Loading : UiState
    data class Success(val outputText: String) : UiState
    data class Error(val errorMessage: String) : UiState
    data class Custom(val state: String) : UiState
}