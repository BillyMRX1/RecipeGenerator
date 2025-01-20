package com.mrx.recipegenerator.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mrx.recipegenerator.core.data.model.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    fun insertHistory(history: History): Long

    @Query("SELECT * FROM history_table ORDER BY timestamp DESC")
    fun getHistories(): Flow<List<History>>

    @Query("DELETE FROM history_table WHERE id = :id")
    fun deleteHistory(id: Int): Int
}