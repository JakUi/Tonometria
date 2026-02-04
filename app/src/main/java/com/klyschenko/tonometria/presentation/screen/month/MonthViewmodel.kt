@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.domain.usecase.DeleteRecordUseCase
import com.klyschenko.tonometria.domain.usecase.EditRecordUseCase
import com.klyschenko.tonometria.domain.usecase.GetAllMonthsRecordsUseCase
import com.klyschenko.tonometria.domain.usecase.GetMonthUseCase
import com.klyschenko.tonometria.domain.usecase.GetYearUseCase
import com.klyschenko.tonometria.domain.usecase.SetMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MonthViewmodel @Inject constructor(
    private val getAllMonthsRecordsUseCase: GetAllMonthsRecordsUseCase,
    private val editRecordUseCase: EditRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val setMonthUseCase: SetMonthUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val getYearUseCase: GetYearUseCase
) : ViewModel() {

    private val selectedYear = getYearUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 2026
        )

    private val selectedMonth = getMonthUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1
        )

    private val _dateLoaded = MutableStateFlow(false)
    val dateLoaded = _dateLoaded.asStateFlow()

    private val _state =
        MutableStateFlow<Map<Int, Map<DayPart, List<PressureData>>>>(
            emptyMap()
        )
    val state = _state.asStateFlow()

    private val _dateState = MutableStateFlow(
        DateState(
            selectedYear.value, selectedMonth.value
        )
    )
    val dateState = _dateState.asStateFlow()

    suspend fun updateMonth(monthNumber: Int) {
        _dateState.update { previousState ->
            val newState = previousState.copy(
                month = monthNumber
            )
            setMonthUseCase(month = monthNumber)
            newState
        }
    }

    fun updateYear(yearNumber: Int) {
        _dateState.update { previousState ->
            val newState = previousState.copy(
                year = yearNumber
            )
            newState
        }
    }

    suspend fun loadRecords() {
        val map = getAllMonthsRecordsUseCase(
            year = dateState.value.year,
            month = dateState.value.month
        ).first()
        _state.value = map
    }

    init {
        viewModelScope.launch {
            val year = getYearUseCase().first()
            val monthNumber = getMonthUseCase().first()
            updateYear(year)
            updateMonth(monthNumber)
            loadRecords()
            _dateLoaded.update { true }
        }
    }

    fun daysInMonth(monthNumber: Int): Int {
        val monthCount = mapOf(
            1 to 31,
            2 to 28,
            3 to 31,
            4 to 30,
            5 to 31,
            6 to 30,
            7 to 31,
            8 to 31,
            9 to 30,
            10 to 31,
            11 to 30,
            12 to 31
        )
        return monthCount[monthNumber] ?: 31
    }

    fun processDateCommand(command: DateCommand) {
        when (command) {
            is DateCommand.ChangeMonth -> {
                viewModelScope.launch {
                    updateMonth(command.month)
                    loadRecords()
                }
            }
        }
    }

    fun processCellCommand(command: CellCommand) {
        viewModelScope.launch {
            when (command) {
                is CellCommand.EditData -> {
                    viewModelScope.launch {
                        editRecordUseCase(
                            year = command.year,
                            month = command.month,
                            day = command.day,
                            wroteAt = command.wroteAt,
                            toUpdate = command.toUpdate
                        )
                    }
                }

                is CellCommand.DeleteRecord -> {
                    viewModelScope.launch {
                        deleteRecordUseCase(
                            year = command.year,
                            month = command.month,
                            day = command.day,
                            wroteAt = command.wroteAt,
                        )
                        loadRecords()
                    }
                }
            }
        }
    }
}

data class DateState(
    val year: Int,
    val month: Int
)

sealed interface DateCommand {

    data class ChangeMonth(val month: Int) : DateCommand
}

sealed interface CellCommand {

    data class EditData(
        val year: Int,
        val month: Int,
        val day: Int,
        val wroteAt: DayPart,
        val toUpdate: ToUpdate
    ) : CellCommand

    data class DeleteRecord(
        val year: Int,
        val month: Int,
        val day: Int,
        val wroteAt: DayPart
    ) : CellCommand
}