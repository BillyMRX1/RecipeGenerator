package com.mrx.recipegenerator.core.data.datasource

import com.mrx.recipegenerator.core.data.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryLocalDataSource {
    suspend fun addHistory(history: History): Boolean
    suspend fun getHistories(): Flow<List<History>>
    suspend fun deleteHistory(id: Int): Boolean
}