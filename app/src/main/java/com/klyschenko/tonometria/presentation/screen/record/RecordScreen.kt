@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria.presentation.screen.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.klyschenko.tonometria.R
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.presentation.ui.textfield.DigitOnlyInputTransformation

@Composable
fun CreateRecord(
    modifier: Modifier = Modifier,
    viewModel: CreateRecordViewModel = hiltViewModel(),
    day: Int,
    dayPart: DayPart,
    onSaveClick: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_data)) }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .navigationBarsPadding() // поднимет кнопку над системной навигацией
                    .imePadding() // поднимет кнопку над клавиатурой
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onClick = {
                    val upper = viewModel.upperPressureState.text.toString().toIntOrNull() ?: return@Button
                    val lower = viewModel.lowerPressureState.text.toString().toIntOrNull() ?: return@Button
                    val pulse = viewModel.pulseState.text.toString().toIntOrNull() ?: return@Button
                    val comment = viewModel.commentState.text
                    viewModel.processCommand(
                        CreateRecordViewModel.RecordCommand.Create(
                            day = day,
                            wroteAt = dayPart,
                            upperPressure = upper,
                            lowerPressure = lower,
                            pulse = pulse,
                            comment = comment.toString()
                        )
                    )
                    onSaveClick()
                },
                shape = RoundedCornerShape(10.dp),
                enabled = viewModel.isSaveEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.1f
                    ),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {

            val (
                upperFR,
                lowerFR,
                pulseFR
            ) = FocusRequester.createRefs()

            Field(text = stringResource(R.string.upper)) {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(upperFR),
                    state = viewModel.upperPressureState,
//                    label = { Text("Upper") },
                    inputTransformation = InputTransformation.maxLength(3)
                        .then(DigitOnlyInputTransformation()),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardAction = KeyboardActionHandler { _ ->
                        lowerFR.requestFocus()
                    }
                )
            }

            Field(text = stringResource(R.string.lower)) {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(lowerFR),
                    state = viewModel.lowerPressureState,
//                    label = { Text("Lower") },
                    inputTransformation = InputTransformation.maxLength(3)
                        .then(DigitOnlyInputTransformation()),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardAction = KeyboardActionHandler { _ ->
                        pulseFR.requestFocus()
                    }
                )
            }

            Field(text = stringResource(R.string.pulse)) {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(pulseFR),
                    state = viewModel.pulseState,
//                    label = { Text("Pulse") },
                    inputTransformation = InputTransformation.maxLength(3)
                        .then(DigitOnlyInputTransformation()),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Field(text = stringResource(R.string.comment)) {
                OutlinedTextField(
                    modifier = Modifier.height(180.dp),
                    state = viewModel.commentState,
                )
            }
        }
    }
}

@Composable
fun Field(
    modifier: Modifier = Modifier,
    text: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(86.dp)
        ) {
            Text(
                text = "$text:",
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier.width(210.dp)
        ) {
            content()
        }
    }
}