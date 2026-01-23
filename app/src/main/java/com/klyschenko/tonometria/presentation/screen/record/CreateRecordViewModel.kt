@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.usecase.AddNewRecordUseCase
import com.klyschenko.tonometria.domain.usecase.GetMonthUseCase
import com.klyschenko.tonometria.domain.usecase.GetYearUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateRecordViewModel @Inject constructor(
    private val addNewRecordUseCase: AddNewRecordUseCase,
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

    private val _dateState = MutableStateFlow(
        DateState(
            selectedYear.value, selectedMonth.value
        )
    )

    val upperPressureState = TextFieldState()
    val lowerPressureState = TextFieldState()
    val pulseState = TextFieldState()

    val isSaveEnabled: Boolean
        get() = upperPressureState.text.isNotBlank() &&
                lowerPressureState.text.isNotBlank() &&
                pulseState.text.isNotBlank()

    init {
        viewModelScope.launch {
            getMonthUseCase().collect { monthNumber ->
                _dateState.update { previousState ->
                    val newState = previousState.copy(
                        month = monthNumber
                    )
                    newState
                }
            }
        }
    }

    fun processCommand(command: RecordCommand) {
        when (command) {
            is RecordCommand.Create -> {
                viewModelScope.launch {
                    addNewRecordUseCase(
                        Record(
                            year = _dateState.value.year,
                            month = _dateState.value.month,
                            day = command.day,
                            wroteAt = command.wroteAt,
                            data = PressureData(
                                upperPressure = command.upperPressure,
                                lowerPressure = command.lowerPressure,
                                pulse = command.pulse,
                                comment = command.comment
                            )
                        )
                    )
                }
            }
        }
    }

    data class DateState(
        val year: Int,
        val month: Int
    )

    sealed interface RecordCommand {

        data class Create(
            val day: Int,
            val wroteAt: DayPart,
            val upperPressure: Int,
            val lowerPressure: Int,
            val pulse: Int,
            val comment: String
        ) : RecordCommand
    }
}