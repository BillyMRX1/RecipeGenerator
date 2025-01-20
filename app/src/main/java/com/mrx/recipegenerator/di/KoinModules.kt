package com.mrx.recipegenerator.di

import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.mrx.recipegenerator.BuildConfig
import com.mrx.recipegenerator.core.data.database.RecipeDatabase
import com.mrx.recipegenerator.core.data.datasource.HistoryLocalDataSource
import com.mrx.recipegenerator.core.data.datasource.HistoryLocalDataSourceImpl
import com.mrx.recipegenerator.core.domain.repository.HistoryRepository
import com.mrx.recipegenerator.core.domain.repository.HistoryRepositoryImpl
import com.mrx.recipegenerator.core.domain.usecase.AddHistoryUseCase
import com.mrx.recipegenerator.core.domain.usecase.DeleteHistoryUseCase
import com.mrx.recipegenerator.core.domain.usecase.GetHistoriesUseCase
import com.mrx.recipegenerator.core.domain.usecase.GetHistoryBasedIdUseCase
import com.mrx.recipegenerator.viewmodel.HistoryDetailViewModel
import com.mrx.recipegenerator.viewmodel.HistoryViewModel
import com.mrx.recipegenerator.viewmodel.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val provideGenerativeAI = module {
    single {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY
        )
    }
}

val provideDatabaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            RecipeDatabase::class.java,
            "recipe_database"
        ).build()
    }

    single { get<RecipeDatabase>().historyDao() }
}

val provideDataSourceModule = module {
    singleOf(::HistoryLocalDataSourceImpl).bind(HistoryLocalDataSource::class)
}

val provideRepositoryModule = module {
    singleOf(::HistoryRepositoryImpl).bind(HistoryRepository::class)
}

val provideUseCaseModule = module {
    singleOf(::AddHistoryUseCase)
    singleOf(::DeleteHistoryUseCase)
    singleOf(::GetHistoriesUseCase)
    singleOf(::GetHistoryBasedIdUseCase)
}

val provideViewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::HistoryDetailViewModel)
}
