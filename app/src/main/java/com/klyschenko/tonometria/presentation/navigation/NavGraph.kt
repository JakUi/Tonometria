package com.klyschenko.tonometria.presentation.navigation

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klyschenko.tonometria.domain.entity.DayPart
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
                onCellClick = { clickedDay, dayPart: DayPart ->
                    navController.navigate(
                        Screen.CreateRecordScreen.createRoute(
                            clickedDay, dayPart
                        )
                    )
                }
            )
        }
        composable(Screen.CreateRecordScreen.route) {
            val day: Int = Screen.CreateRecordScreen.getDay(it.arguments)
            val dayPart: DayPart = Screen.CreateRecordScreen.getDayPart(it.arguments)
            CreateRecord(
                day = day,
                dayPart = dayPart,

                onSaveClick = {
                    navController.navigate(
                        Screen.MonthScreen.route
                    )
                }
            )
        }
    }
}

sealed class Screen(val route: String) {

    data object MonthScreen : Screen(route = "month")

    data object CreateRecordScreen : Screen(route = "create/{day}/{dayPart}") {

        fun createRoute(day: Int, dayPart: DayPart): String {
            return "create/$day/$dayPart"
        }

        fun getDay(arguments: Bundle?): Int {
            return arguments?.getString("day")?.toInt() ?: 1
        }

        fun getDayPart(arguments: Bundle?): DayPart {
            val dayPartString = arguments?.getString("dayPart")
            Log.d("Debug", "dayPartString: $dayPartString")
            val dayPart: DayPart = DayPart.getByName(dayPartString) ?: DayPart.MORNING
            Log.d("Debug", "dayPart: $dayPart")
            return dayPart
        }
    }
}