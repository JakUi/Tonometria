@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.month

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.usecase.GetAllMonthsRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MonthViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllMonthsRecordsUseCase: GetAllMonthsRecordsUseCase
) : ViewModel() {

    companion object {
        private const val KEY_MONTH = "month"
        private const val KEY_YEAR = "year"
    }

    private val selectedYear = savedStateHandle.getStateFlow(KEY_YEAR, 2026)
    private val selectedMonth = savedStateHandle.getStateFlow(KEY_MONTH, 1)

    private val _state = MutableStateFlow(
        listOf<Record>(
            Record(
                recordId = 4,
                day = 8,
                month = 1,
                year = 2026,
                upperPressure = 121,
                lowerPressure = 79,
                pulse = 77,
                wroteAt = DayPart.EVENING,
                comment = "!!!"
            )
        )
    )

    val state = _state.asStateFlow()

    private fun getRecords() { // Нужно править тут. Сейчас работает не верно т.к. в State добавляется только последний параметр.
        getAllMonthsRecordsUseCase(month = selectedMonth.value, year = selectedYear.value)
            .onEach { _ ->
                _state.update {
                    previousState ->
                    val newState = previousState
                    newState
                }
            }.launchIn(viewModelScope)
    }

    init {
        getRecords()
    }

    fun onMonthSelected(month: Int) {
        savedStateHandle[KEY_MONTH] = month
    }

    fun onYearSelected(year: Int) {
        savedStateHandle[KEY_YEAR] = year
    }

    fun processCommand(command: MonthCommand) {
        viewModelScope.launch {
            when (command) {
                is MonthCommand.ChangeMonth -> onMonthSelected(month = command.month)
                is MonthCommand.OpenRecord -> TODO()
            }
        }
    }
}

sealed interface MonthCommand {

    data class ChangeMonth(val month: Int) : MonthCommand

    data class OpenRecord(val recordId: Int, val wroteAt: DayPart) : MonthCommand
}
