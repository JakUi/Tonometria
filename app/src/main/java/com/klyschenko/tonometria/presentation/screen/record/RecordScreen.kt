@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria.presentation.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.klyschenko.tonometria.R
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.presentation.ui.textfield.DigitOnlyInputTransformation
import com.klyschenko.tonometria.presentation.ui.theme.Aquamarine
import com.klyschenko.tonometria.presentation.ui.theme.Blue
import com.klyschenko.tonometria.presentation.ui.theme.CarmineRed
import com.klyschenko.tonometria.presentation.ui.theme.Mustard
import com.klyschenko.tonometria.presentation.ui.theme.Peach40
import com.klyschenko.tonometria.presentation.ui.theme.Peach80
import com.klyschenko.tonometria.presentation.ui.theme.Purple40

@Composable
fun CreateRecord(
    modifier: Modifier = Modifier,
    day: Int,
    dayPart: DayPart,
    onSaveClick: () -> Unit
) {
    val viewModel: CreateRecordViewModel =
        hiltViewModel<CreateRecordViewModel, CreateRecordViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(day = day, wroteAt = dayPart)
            }
        )

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
                    val upper =
                        viewModel.upperPressureState.text.toString().toIntOrNull() ?: return@Button
                    val lower =
                        viewModel.lowerPressureState.text.toString().toIntOrNull() ?: return@Button
                    val pulse = viewModel.pulseState.text.toString().toIntOrNull() ?: return@Button
                    val comment = viewModel.commentState.text
                    val commentColor = viewModel.commentColorState.value
                    viewModel.processCommand(
                        CreateRecordViewModel.RecordCommand.Create(
                            day = day,
                            wroteAt = dayPart,
                            upperPressure = upper,
                            lowerPressure = lower,
                            pulse = pulse,
                            comment = comment.toString(),
                            commentColor = commentColor
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

        val state by viewModel.screenState.collectAsStateWithLifecycle()

        when (val s = state) {
            is CreateRecordViewModel.ScreenState.Content -> {
                LaunchedEffect(s) {
                    viewModel.upperPressureState.edit { replace(0, length, s.upper) }
                    viewModel.lowerPressureState.edit { replace(0, length, s.lower) }
                    viewModel.pulseState.edit { replace(0, length, s.pulse) }
                    viewModel.commentState.edit { replace(0, length, s.comment) }
                }
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

                    Spacer(modifier = Modifier.height(16.dp))

                    var selectedIndex by remember { mutableIntStateOf(-1) }
                    val colors = listOf(
                        Peach80,
                        Peach40,
                        Aquamarine,
                        Blue,
                        Mustard
                    )

                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(60.dp)
                    ) {
                        colors.forEachIndexed { index, color ->
                            AddColor(
                                backgroundColor = color,
                                isSelected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    viewModel.commentColorState.value = color.toArgb()                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(60.dp)
                    ) {
                        AddColor(
                            backgroundColor = CarmineRed,
                            isSelected = selectedIndex == 5,
                            onClick = { selectedIndex = 5 }
                        )
                    }
                }
            }

            CreateRecordViewModel.ScreenState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(140.dp)
                            .height(100.dp),
                        model = R.drawable.loader,
                        contentDescription = stringResource(R.string.screen_loader_image),
                        contentScale = ContentScale.Fit
                    )
                }
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

@Composable
fun AddColor(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.width(16.dp))

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .border(
                5.dp,
                if (isSelected) Purple40 else backgroundColor,
                CircleShape
            )
            .background(backgroundColor, CircleShape)
            .clickable { onClick() }
    )
}
