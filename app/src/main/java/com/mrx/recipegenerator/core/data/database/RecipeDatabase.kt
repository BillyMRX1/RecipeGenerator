package com.mrx.recipegenerator.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrx.recipegenerator.core.data.database.dao.HistoryDao
import com.mrx.recipegenerator.core.data.model.History

@Database(entities = [History::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}