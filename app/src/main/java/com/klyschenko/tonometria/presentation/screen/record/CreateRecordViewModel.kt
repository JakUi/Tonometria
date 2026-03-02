@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.record

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayData
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.usecase.AddNewRecordUseCase
import com.klyschenko.tonometria.domain.usecase.GetMonthUseCase
import com.klyschenko.tonometria.domain.usecase.GetSingleRecordUseCase
import com.klyschenko.tonometria.domain.usecase.GetYearUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = CreateRecordViewModel.Factory::class)
class CreateRecordViewModel @AssistedInject constructor(
    private val addNewRecordUseCase: AddNewRecordUseCase,
    private val getMonthUseCase: GetMonthUseCase,
    private val getYearUseCase: GetYearUseCase,
    private val getSingleRecordUseCase: GetSingleRecordUseCase,
    @Assisted("day") day: Int,
    @Assisted("wroteAt") wroteAt: DayPart
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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("day") day: Int,
            @Assisted("wroteAt") wroteAt: DayPart
        ): CreateRecordViewModel
    }

    private val record = MutableStateFlow<DayData?>(null)
    private val upper = MutableStateFlow<String>("")
    private val lower = MutableStateFlow<String>("")
    private val pulse = MutableStateFlow<String>("")
    private val comment = MutableStateFlow<String>("")

    fun loadRecord(year: Int, month: Int, day: Int, wroteAt: DayPart) {
        viewModelScope.launch {
            record.value = getSingleRecordUseCase(year, month, day, wroteAt)
            upperPressureState.edit {
                replace(0, length, record.value?.data?.upperPressure?.toString().orEmpty())
            }
            lowerPressureState.edit {
                replace(0, length, record.value?.data?.lowerPressure?.toString().orEmpty())
            }
            pulseState.edit {
                replace(0, length, record.value?.data?.pulse?.toString().orEmpty())
            }
            commentState.edit {
                replace(0, length, record.value?.data?.comment.orEmpty())
            }
        }
    }

    val upperPressureState = TextFieldState(upper.value)
    val lowerPressureState = TextFieldState(lower.value)
    val pulseState = TextFieldState(pulse.value)
    val commentState = TextFieldState(comment.value)

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
        loadRecord(selectedYear.value, selectedMonth.value, day, wroteAt)
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