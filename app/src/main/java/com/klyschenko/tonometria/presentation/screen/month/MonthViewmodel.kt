@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.month

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.domain.usecase.AddNewRecordUseCase
import com.klyschenko.tonometria.domain.usecase.DeleteRecordUseCase
import com.klyschenko.tonometria.domain.usecase.EditRecordUseCase
import com.klyschenko.tonometria.domain.usecase.GetAllMonthsRecordsUseCase
import com.klyschenko.tonometria.domain.usecase.GetMonthUseCase
import com.klyschenko.tonometria.domain.usecase.GetYearUseCase
import com.klyschenko.tonometria.domain.usecase.SetMonthUseCase
import com.klyschenko.tonometria.domain.usecase.SetYearUseCase
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
    private val addNewRecordUseCase: AddNewRecordUseCase,
    private val editRecordUseCase: EditRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val setMonthUseCase: SetMonthUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val setYearUseCase: SetYearUseCase,
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

    private val _state =
        MutableStateFlow<Map<Int, Map<DayPart, List<PressureData>>>>(emptyMap())
    val state = _state.asStateFlow()

    private val _dateState = MutableStateFlow(
        DateState(
            selectedYear.value, selectedMonth.value
        )
    )
    val dateState = _dateState.asStateFlow()


    fun updateMonth(monthNumber: Int) {
        viewModelScope.launch {
            _dateState.update { previousState ->
                val newState = previousState.copy(
                    month = monthNumber
                )
                Log.d("Debug", "Update Month method, new state is: $newState")
                setMonthUseCase(month = monthNumber)
                loadRecords()
                newState
            }
        }
    }

    fun updateYear(yearNumber: Int) {
        viewModelScope.launch {
            _dateState.update { previousState ->
                val newState = previousState.copy(
                    year = yearNumber
                )
                setYearUseCase(year = yearNumber)
                newState
            }
        }
    }

    fun loadRecords() {
        viewModelScope.launch {
            Log.d("Debug", "Selected month: ${selectedMonth.first()}")
            getAllMonthsRecordsUseCase(
                year = selectedYear.value,
                month = selectedMonth.first()
            ).collect { map ->
                _state.value = map
            }
        }
    }

    init {
        viewModelScope.launch {
            getMonthUseCase().collect { value ->
                Log.d("DataStore", "Month from DataStore = $value")
            }
        }

        updateMonth(selectedMonth.value)
        loadRecords()
    }

    fun processDateCommand(command: DateCommand) {
        when (command) {
            is DateCommand.ChangeMonth -> {
                updateMonth(command.month)
                Log.d("Debug", "Month was updated")
                loadRecords()
            }

            is DateCommand.ChangeYear -> TODO()
        }
    }

    fun processCellCommand(command: CellCommand) {
        viewModelScope.launch {
            when (command) {
                is CellCommand.AddData -> {
                    viewModelScope.launch {
                        addNewRecordUseCase(record = command.record)
                    }
                }

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

    data class ChangeYear(val year: Int) : DateCommand

    data class ChangeMonth(val month: Int) : DateCommand
}

sealed interface CellCommand {

    data class AddData(val record: Record) : CellCommand

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