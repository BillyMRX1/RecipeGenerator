package com.mrx.recipegenerator.core.domain.usecase

import com.mrx.recipegenerator.core.domain.repository.HistoryRepository

class GetHistoryByIdUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(id: Int) = repository.getHistoryById(id)
}