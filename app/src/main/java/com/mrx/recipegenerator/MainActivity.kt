package com.mrx.recipegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mrx.recipegenerator.ui.screen.MainScreen
import com.mrx.recipegenerator.ui.theme.RecipeGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeGeneratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}