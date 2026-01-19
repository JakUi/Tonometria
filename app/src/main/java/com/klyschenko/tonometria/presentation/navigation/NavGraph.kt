package com.klyschenko.tonometria.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klyschenko.tonometria.presentation.screen.month.Month
import com.klyschenko.tonometria.presentation.screen.record.CreateRecord

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.MonthScreen.route
    ) {
        composable(Screen.MonthScreen.route) {
            Month(
                onCellClick = {
                    navController.navigate(Screen.CreateRecordScreen.route)
                }
            )
        }
        composable(Screen.CreateRecordScreen.route) {
            CreateRecord(
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }

    }
}

sealed class Screen(val route: String) {

    data object MonthScreen : Screen(route = "month")

    data object CreateRecordScreen : Screen(route = "create")
}