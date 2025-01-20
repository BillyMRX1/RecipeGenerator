package com.mrx.recipegenerator.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mrx.recipegenerator.ui.screen.HistoryDetailScreen
import com.mrx.recipegenerator.ui.screen.HistoryScreen
import com.mrx.recipegenerator.ui.screen.MainScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(200)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(200)
            )
        },
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController)
        }
        composable(
            Screen.HistoryDetail.route + "/{historyId}",
            arguments = listOf(navArgument("historyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val historyId = backStackEntry.arguments?.getInt("historyId")
            HistoryDetailScreen(navController, historyId)
        }
    }
}