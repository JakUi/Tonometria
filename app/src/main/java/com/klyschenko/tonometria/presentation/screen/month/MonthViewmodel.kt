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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MonthViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllMonthsRecordsUseCase: GetAllMonthsRecordsUseCase,
    private val addNewRecordUseCase: AddNewRecordUseCase,
    private val editRecordUseCase: EditRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
) : ViewModel() {

    companion object {
        private const val KEY_MONTH = "month"
        private const val KEY_YEAR = "year"
    }

    private val selectedYear = savedStateHandle.getStateFlow(KEY_YEAR, 2026)
    private val selectedMonth = savedStateHandle.getStateFlow(KEY_MONTH, 1)

    private val _state =
        MutableStateFlow<Map<Int, Map<DayPart, List<PressureData>>>>(emptyMap())
    val state = _state.asStateFlow()

    fun forTestAddRecordsToDB() {
        val record = Record(
            day = 12,
            month = 1,
            year = 2026,
            wroteAt = DayPart.MORNING,
            data = PressureData(120, 80, 67, "")
        )

        val record2 = Record(
            day = 13,
            month = 1,
            year = 2026,
            wroteAt = DayPart.DAY,
            data = PressureData(119, 79, 64, "Second")
        )

        viewModelScope.launch {
            addNewRecordUseCase(record)
            addNewRecordUseCase(record2)
            editRecordUseCase(year = 2026, month =1, day = 12, wroteAt = DayPart.MORNING, toUpdate = ToUpdate.Comment("Cool"))
        }
    }

    fun loadRecords() {
        viewModelScope.launch {
            getAllMonthsRecordsUseCase(
                year = selectedYear.value,
                month = selectedMonth.value
            ).collect { map ->
                _state.value = map
                Log.d("Debug2", "state=${_state.value}")
            }
        }
    }

    init {
//        forTestAddRecordsToDB()
        loadRecords()
    }

    fun onMonthSelected(month: Int) {
        savedStateHandle[KEY_MONTH] = month
    }

    fun onYearSelected(year: Int) {
        savedStateHandle[KEY_YEAR] = year
    }

    fun processMonthCommand(command: MonthCommand) {
        viewModelScope.launch {
            when (command) {
                is MonthCommand.ChangeMonth -> onMonthSelected(month = command.month)
                is MonthCommand.OpenRecord -> TODO()
            }
        }
    }

    fun processCellCommand(command: CellCommand) {
        viewModelScope.launch {
            when (command) {
                is CellCommand.AddData -> addNewRecordUseCase(record = command.record)
                is CellCommand.EditData -> {
                    editRecordUseCase(
                        year = command.year,
                        month = command.month,
                        day = command.day,
                        wroteAt = command.wroteAt,
                        toUpdate = command.toUpdate
                    )
                }
                is CellCommand.DeleteRecord -> {
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

sealed interface MonthCommand {

    data class ChangeMonth(val month: Int) : MonthCommand

    data class OpenRecord(val recordId: Int, val wroteAt: DayPart) : MonthCommand
}

sealed interface CellCommand {

    data class AddData(val record: Record): CellCommand

    data class EditData(
        val year: Int,
        val month: Int,
        val day: Int,
        val wroteAt: DayPart,
        val toUpdate: ToUpdate
    ): CellCommand

    data class DeleteRecord(
        val year: Int,
        val month: Int,
        val day: Int,
        val wroteAt: DayPart
    ): CellCommand
}