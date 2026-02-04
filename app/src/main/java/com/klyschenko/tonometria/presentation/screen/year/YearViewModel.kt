@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.year

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.usecase.SetYearUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class YearViewModel @Inject constructor(
    private val setYearUseCase: SetYearUseCase,
) : ViewModel() {

    val years = listOf(2026, 2027, 2028, 2029, 2030, 2031, 2032, 2033, 2034, 2035)

    fun processDateCommand(command: DateCommand) {
        when (command) {
            is DateCommand.ChangeYear -> {
                viewModelScope.launch {
                    setYearUseCase(year = command.year)
                }
            }
        }
    }
}

sealed interface DateCommand {

    data class ChangeYear(val year: Int) : DateCommand
}
