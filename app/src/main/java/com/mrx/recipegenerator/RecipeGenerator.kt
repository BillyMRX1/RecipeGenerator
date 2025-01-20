package com.mrx.recipegenerator

import android.app.Application
import com.mrx.recipegenerator.di.provideDataSourceModule
import com.mrx.recipegenerator.di.provideDatabaseModule
import com.mrx.recipegenerator.di.provideGenerativeAI
import com.mrx.recipegenerator.di.provideRepositoryModule
import com.mrx.recipegenerator.di.provideUseCaseModule
import com.mrx.recipegenerator.di.provideViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RecipeGenerator : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RecipeGenerator)
            modules(
                provideGenerativeAI,
                provideDatabaseModule,
                provideDataSourceModule,
                provideRepositoryModule,
                provideUseCaseModule,
                provideViewModelModule
            )
        }
    }
}
