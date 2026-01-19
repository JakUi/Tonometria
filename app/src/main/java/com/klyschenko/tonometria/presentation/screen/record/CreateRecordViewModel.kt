@file:OptIn(ExperimentalCoroutinesApi::class)

package com.klyschenko.tonometria.presentation.screen.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.entity.Record
import com.klyschenko.tonometria.domain.usecase.AddNewRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateRecordViewModel @Inject constructor(
    private val addNewRecordUseCase: AddNewRecordUseCase

) : ViewModel() {

    private val _state = MutableStateFlow<CreateRecordState>(CreateRecordState.Creation())
    val state = _state.asStateFlow()

    val upperPressureState = TextFieldState()
    val lowerPressureState = TextFieldState()
    val pulseState = TextFieldState()

    val isSaveEnabled: Boolean
        get() = upperPressureState.text.isNotBlank() &&
                lowerPressureState.text.isNotBlank() &&
                pulseState.text.isNotBlank()


    fun processCommand(command: RecordCommand) {
        when (command) {
            is RecordCommand.Create -> {
                viewModelScope.launch {
                    addNewRecordUseCase(
                        Record(
                            command.year,
                            command.month,
                            command.day,
                            command.wroteAt,
                            data = PressureData(
                                upperPressure = command.upperPressure,
                                lowerPressure = command.lowerPressure,
                                pulse = command.pulse,
                                comment = command.comment
                            )
                        )
                    )
                    _state.value = CreateRecordState.Finished
                }
            }
        }
    }

    sealed interface RecordCommand {

        data class Create(
            val year: Int,
            val month: Int,
            val day: Int,
            val wroteAt: DayPart,
            val upperPressure: Int,
            val lowerPressure: Int,
            val pulse: Int,
            val comment: String
        ) : RecordCommand
    }

    sealed interface CreateRecordState {

        data class Creation(
            val year: Int = 2026,
            val month: Int = 1,
            val day: Int = 1,
            val wroteAt: DayPart = DayPart.MORNING,
            val upperPressure: Int = 120,
            val lowerPressure: Int = 80,
            val pulse: Int = 67,
            val comment: String? = ""
        ): CreateRecordState

        data object Finished : CreateRecordState
    }
}