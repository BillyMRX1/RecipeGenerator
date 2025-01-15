package com.mrx.recipegenerator.di

import com.google.ai.client.generativeai.GenerativeModel
import com.mrx.recipegenerator.BuildConfig
import com.mrx.recipegenerator.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val provideGenerativeAI = module {
    single {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY
        )
    }
}

val provideViewModelModule = module {
    viewModelOf(::MainViewModel)
}