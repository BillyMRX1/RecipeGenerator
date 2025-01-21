package com.mrx.recipegenerator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrx.recipegenerator.core.data.model.History
import com.mrx.recipegenerator.core.domain.usecase.DeleteHistoryUseCase
import com.mrx.recipegenerator.core.domain.usecase.GetHistoryByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryDetailViewModel(
    private val getHistoryByIdUseCase: GetHistoryByIdUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _history = MutableStateFlow<History?>(null)
    val history: StateFlow<History?> = _history.asStateFlow()

    fun getHistoryById(id: Int) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                getHistoryByIdUseCase.invoke(id).collect { result ->
                    _history.value = result
                    _uiState.value = UiState.Success("History fetched successfully")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error fetching history: ${e.localizedMessage}")
            }
        }
    }

    fun deleteHistoryById(id: Int) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deleteSuccess = deleteHistoryUseCase.invoke(id)
                if (deleteSuccess) {
                    _uiState.value = UiState.Custom(DELETE_HISTORY)
                } else {
                    _uiState.value = UiState.Error("Failed to delete history")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error fetching history: ${e.localizedMessage}")
            }
        }
    }

    companion object {
        const val DELETE_HISTORY = "delete_history"
    }
}