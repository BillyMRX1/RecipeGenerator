package com.mrx.recipegenerator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrx.recipegenerator.core.data.model.History
import com.mrx.recipegenerator.core.domain.usecase.GetHistoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val getHistoriesUseCase: GetHistoriesUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _histories = MutableStateFlow<List<History>>(emptyList())
    val histories: StateFlow<List<History>> = _histories.asStateFlow()

    fun getHistories() {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                getHistoriesUseCase.invoke().collect { result ->
                    _histories.value = result

                    if (result.isNotEmpty()) {
                        _uiState.value = UiState.Success("Fetched ${result.size} histories")
                    } else {
                        _uiState.value = UiState.Error("No histories found.")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error fetching histories: ${e.localizedMessage}")
            }
        }
    }
}

