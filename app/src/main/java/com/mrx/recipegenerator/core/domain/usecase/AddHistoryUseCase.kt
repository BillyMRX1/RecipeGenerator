package com.mrx.recipegenerator.core.domain.usecase

import android.net.Uri
import com.mrx.recipegenerator.core.data.model.History
import com.mrx.recipegenerator.core.domain.repository.HistoryRepository

class AddHistoryUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(prompt: String, output: String, imageUri: Uri?) {
        val history = History(prompt = prompt, output = output, imageUri = imageUri.toString())
        repository.addHistory(history)
    }
}