@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.month

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MonthViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllMonthsRecordsUseCase: GetAllMonthsRecordsUseCase,
    private val addNewRecordUseCase: AddNewRecordUseCase,
    private val editRecordUseCase: EditRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val setMonthUseCase: SetMonthUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val setYearUseCase: SetYearUseCase,
    private val getYearUseCase: GetYearUseCase
) : ViewModel() {

//    companion object {
//        private const val KEY_MONTH = "month"
//        private const val KEY_YEAR = "year"
//    }
//
//    private val selectedYear = savedStateHandle.getStateFlow(KEY_YEAR, 2026)
//    private val selectedMonth = savedStateHandle.getStateFlow(KEY_MONTH, 1)

    private val selectedYear = getYearUseCase()
    private val selectedMonth = getMonthUseCase()

    private val _state =
        MutableStateFlow<Map<Int, Map<DayPart, List<PressureData>>>>(emptyMap())
    val state = _state.asStateFlow()

    val _dateState = MutableStateFlow(DateState())
    val dateState = _dateState.asStateFlow()

//    fun updateMonth() {
//        onMonthSelected(selectedMonth.value)
//        _monthState.update { previousState ->
//            val newState = previousState.copy(
//                year = selectedYear.value,
//                month = selectedMonth.value
//            )
//            Log.d("Debug", "Update settings method, previous state is: $newState")
//            viewModelScope.launch {
//                setMonthUseCase(month = selectedMonth.value)
//            }
//            newState
//        }
//    }

    fun updateMonth(monthNumber: Int) {
        viewModelScope.launch {
            _dateState.update { previousState ->
                val newState = previousState.copy(
                    month = monthNumber
                )
                Log.d("Debug", "Update Month method, new state is: $newState")
                setMonthUseCase(month = monthNumber)
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
            getAllMonthsRecordsUseCase(
                year = selectedYear.first(),
                month = selectedMonth.first()
            ).collect { map ->
                _state.value = map
            }
        }
        Log.d("Debug", "Current state is: ${_state.value}")
    }

    init {
        loadRecords()
    }


//    fun onYearSelected(year: Int) {
//        savedStateHandle[KEY_YEAR] = year
//        viewModelScope.launch {
//            setYearUseCase(year)
//        }
//    }
//
//    fun onMonthSelected(month: Int) {
//        savedStateHandle[KEY_MONTH] = month
//        viewModelScope.launch {
//            setMonthUseCase(month)
//        }
//    }

    fun processDateCommand(command: DateCommand) {
        when (command) {
            is DateCommand.ChangeMonth -> {
                updateMonth(command.month)
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
    val year: Int = 2026,
    val month: Int = 1
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