package com.mrx.recipegenerator.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main_screen")
    data object History : Screen("history_screen")
    data object HistoryDetail : Screen("history_detail_screen")
}