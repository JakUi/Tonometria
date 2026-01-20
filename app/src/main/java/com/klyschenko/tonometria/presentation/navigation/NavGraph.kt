package com.klyschenko.tonometria.presentation.navigation

import android.os.Bundle
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
                onCellClick = { clickedDay ->
                    navController.navigate(Screen.CreateRecordScreen.createRoute(clickedDay))
                }
            )
        }
        composable(Screen.CreateRecordScreen.route) {
            val day: Int = Screen.CreateRecordScreen.getDay(it.arguments)
            CreateRecord(
                day = day,
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object MonthScreen : Screen(route = "month")

    data object CreateRecordScreen : Screen(route = "create/{day}") {

        fun createRoute(day: Int): String {
            return "create/$day"
        }

        fun getDay(arguments: Bundle?): Int {
            return arguments?.getString("day")?.toInt() ?: 1
        }
    }
}