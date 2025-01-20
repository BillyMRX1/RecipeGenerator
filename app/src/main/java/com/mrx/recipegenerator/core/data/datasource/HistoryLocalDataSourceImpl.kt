package com.mrx.recipegenerator.core.data.datasource

import com.mrx.recipegenerator.core.data.database.dao.HistoryDao
import com.mrx.recipegenerator.core.data.model.History
import kotlinx.coroutines.flow.Flow

class HistoryLocalDataSourceImpl(private val dao: HistoryDao) : HistoryLocalDataSource {
    override suspend fun addHistory(history: History): Boolean = dao.insertHistory(history) > 0

    override suspend fun getHistories(): Flow<List<History>> = dao.getHistories()

    override suspend fun deleteHistory(id: Int): Boolean = dao.deleteHistory(id) > 0

    override suspend fun getHistoryById(id: Int): Flow<History> = dao.getHistoryById(id)
}