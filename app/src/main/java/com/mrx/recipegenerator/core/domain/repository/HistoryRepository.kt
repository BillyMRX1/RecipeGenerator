package com.mrx.recipegenerator.core.domain.repository

import com.mrx.recipegenerator.core.data.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addHistory(history: History): Boolean
    suspend fun getHistories(): Flow<List<History>>
    suspend fun deleteHistory(id: Int): Boolean
}