package com.mrx.recipegenerator.core.domain.usecase

import com.mrx.recipegenerator.core.domain.repository.HistoryRepository

class GetHistoriesUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke() = repository.getHistories()
}