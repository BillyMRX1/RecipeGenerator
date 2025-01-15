package com.mrx.recipegenerator

import android.app.Application
import com.mrx.recipegenerator.di.provideGenerativeAI
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
                provideViewModelModule
            )
        }
    }
}
