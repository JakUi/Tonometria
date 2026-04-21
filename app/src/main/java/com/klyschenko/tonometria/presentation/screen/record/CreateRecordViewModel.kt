@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.record

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    val upperPressureState = TextFieldState()
    val lowerPressureState = TextFieldState()
    val pulseState = TextFieldState()
    val commentState = TextFieldState()
    val commentColorState = MutableStateFlow<Int?>(null)

    val isSaveEnabled: Boolean
        get() = upperPressureState.text.isNotBlank() &&
                lowerPressureState.text.isNotBlank() &&
                pulseState.text.isNotBlank()

    init {
        viewModelScope.launch {
            val monthNumber = getMonthUseCase().firstOrNull() ?: 1
            _dateState.update { previousState ->
                val newState = previousState.copy(
                    month = monthNumber
                )
                newState
            }

            val record = getSingleRecordUseCase(
                year = _dateState.value.year,
                month = _dateState.value.month,
                day = day,
                wroteAt = wroteAt
            )

            _screenState.value = ScreenState.Content(
                upper = record.data.upperPressure.let { if (it == 0) "" else it.toString() },
                lower = record.data.lowerPressure.let { if (it == 0) "" else it.toString() },
                pulse = record.data.pulse.let { if (it == 0) "" else it.toString() },
                comment = record.data.comment.orEmpty(),
                commentColor = record.data.commentColor
            )
            commentColorState.update {  record.data.commentColor }
            Log.d("Debug", "Record data commentColor: ${record.data.commentColor}")
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
                                comment = command.comment,
                                commentColor = command.commentColor
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

    sealed interface ScreenState {
        data object Loading : ScreenState
        data class Content(
            val upper: String,
            val lower: String,
            val pulse: String,
            val comment: String,
            val commentColor: Int?
        ) : ScreenState
    }

    sealed interface RecordCommand {

        data class Create(
            val day: Int,
            val wroteAt: DayPart,
            val upperPressure: Int,
            val lowerPressure: Int,
            val pulse: Int,
            val comment: String,
            val commentColor: Int?
        ) : RecordCommand
    }
}