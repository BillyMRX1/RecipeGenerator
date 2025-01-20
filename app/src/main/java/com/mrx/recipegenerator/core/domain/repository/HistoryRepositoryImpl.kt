package com.mrx.recipegenerator.core.domain.repository

import com.mrx.recipegenerator.core.data.datasource.HistoryLocalDataSource
import com.mrx.recipegenerator.core.data.model.History
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(private val dataSource: HistoryLocalDataSource) : HistoryRepository {
    override suspend fun addHistory(history: History): Boolean = dataSource.addHistory(history)

    override suspend fun getHistories(): Flow<List<History>> = dataSource.getHistories()

    override suspend fun deleteHistory(id: Int): Boolean = dataSource.deleteHistory(id)
}
