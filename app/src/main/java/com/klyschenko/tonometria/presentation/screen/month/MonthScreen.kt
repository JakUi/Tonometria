@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria.presentation.screen.month

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.klyschenko.tonometria.R
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.pressureData.DataType
import com.klyschenko.tonometria.domain.pressureData.colorValue
import com.klyschenko.tonometria.domain.pressureData.valueOf
import com.klyschenko.tonometria.presentation.mapper.asString
import com.klyschenko.tonometria.presentation.util.getMonthName
import com.klyschenko.tonometria.presentation.util.getMonthNumber
import com.klyschenko.tonometria.presentation.util.getYearAsString
import kotlin.Int
import kotlin.Unit


@Composable
fun Month(
    viewModel: MonthViewmodel = hiltViewModel(),
    navController: NavController,
    onCellClick: (day: Int, dayPart: DayPart) -> Unit,
    onYearClick: () -> Unit
) {
    val dateState by viewModel.dateState.collectAsState()
    val settingState by viewModel.settingsState.collectAsState()
    val dateLoadedState by viewModel.dateLoaded.collectAsState()
    val context = LocalContext.current

    if (!dateLoadedState) {
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
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        val interactionSource = remember { MutableInteractionSource() }

                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                if (interaction is PressInteraction.Release) onYearClick()
                            }
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .width(80.dp)
                                .height(56.dp)
                                .clickable(
                                    enabled = true,
                                    onClick = onYearClick
                                ),
                            state = rememberTextFieldState(
                                initialText = dateState.year.getYearAsString()
                            ),
                            readOnly = true,
                            interactionSource = interactionSource,
                            lineLimits = TextFieldLineLimits.SingleLine,
                            textStyle = TextStyle(
                                fontSize = 20.sp
                            ),
                            shape = RoundedCornerShape(12.dp),
                        )
                    },
                    actions = {
                        Spacer(modifier = Modifier.width(8.dp))

                        Dropdown(
                            width = 92,
                            options = listOf(
                                "9",
                                "10",
                                "11",
                                "12",
                                "13",
                                "14",
                                "15"
                            ),
                            selected = settingState.fontSize.toString(),
                            onSelected = {
                                viewModel.processSettingsCommand(
                                    command =
                                        SettingsCommand.ChangeFontSize(
                                            fontSize = it.toIntOrNull() ?: 12
                                        )
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Dropdown(
                            options = listOf(
                                stringResource(R.string.january),
                                stringResource(R.string.february),
                                stringResource(R.string.march),
                                stringResource(R.string.april),
                                stringResource(R.string.may),
                                stringResource(R.string.june),
                                stringResource(R.string.july),
                                stringResource(R.string.august),
                                stringResource(R.string.september),
                                stringResource(R.string.october),
                                stringResource(R.string.november),
                                stringResource(R.string.december)
                            ),
                            selected = dateState.month.getMonthName().asString(context),
                            onSelected = {
                                viewModel.processDateCommand(command = DateCommand.ChangeMonth(month = it.getMonthNumber()))
                            }
                        )
                    }
                )
            }
        ) { innerPadding ->

            val listState = rememberSaveable(saver = LazyListState.Saver) {
                LazyListState()
            }

            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val shouldRefresh by savedStateHandle
                ?.getStateFlow("refresh_month", false)
                ?.collectAsStateWithLifecycle()
                ?: remember { mutableStateOf(false) }

            LaunchedEffect(shouldRefresh) {
                if (shouldRefresh) {
                    viewModel.loadRecords()
                    savedStateHandle?.set("refresh_month", false)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = listState,
                contentPadding = PaddingValues(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = (1..viewModel.daysInMonth(dateState.month)).toList()
                ) { _, item ->
                    DayRow(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                        index = item,
                        onCellClick = onCellClick
                    )
                }
            }
        }
    }
}

@Composable
fun DayRow(
    modifier: Modifier = Modifier,
    index: Int,
    viewModel: MonthViewmodel,
    onCellClick: (day: Int, dayPart: DayPart) -> Unit
) {
    val rowShape = RoundedCornerShape(8.dp)
    val state = viewModel.state.collectAsState()
    val settingsState by viewModel.settingsState.collectAsState()
    val monthState by viewModel.dateState.collectAsState()
    val defaultColor: Int = MaterialTheme.colorScheme.surface.toArgb()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = rowShape
                )
                .padding(end = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$index")
            }

            val morningData = state.value[index]?.get(DayPart.MORNING)
            val dayData = state.value[index]?.get(DayPart.DAY)
            val eveningData = state.value[index]?.get(DayPart.EVENING)
            val morningDataCommentColor = getCellColor(morningData, defaultColor)
            val dayDataDataCommentColor = getCellColor(dayData, defaultColor)
            val eveningDataCommentColor = getCellColor(eveningData, defaultColor)
            Cell(
                modifier = Modifier.weight(1f),
                upperPressure = morningData.valueOf(DataType.UPPER),
                lowerPressure = morningData.valueOf(DataType.LOWER),
                pulse = morningData.valueOf(DataType.PULSE),
                cellColor = morningDataCommentColor,
                fontSize = settingsState.fontSize,
                onCellClick = {
                    onCellClick(index, DayPart.MORNING)
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = monthState.year,
                            month = monthState.month,
                            day = index,
                            wroteAt = DayPart.MORNING
                        )
                    )
                }
            )
            Cell(
                modifier = Modifier.weight(1f),
                upperPressure = dayData.valueOf(DataType.UPPER),
                lowerPressure = dayData.valueOf(DataType.LOWER),
                pulse = dayData.valueOf(DataType.PULSE),
                cellColor = dayDataDataCommentColor,
                fontSize = settingsState.fontSize,
                onCellClick = {
                    onCellClick(index, DayPart.DAY)
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = monthState.year,
                            month = monthState.month,
                            day = index,
                            wroteAt = DayPart.DAY
                        )
                    )
                }
            )
            Cell(
                modifier = Modifier.weight(1f),
                upperPressure = eveningData.valueOf(DataType.UPPER),
                lowerPressure = eveningData.valueOf(DataType.LOWER),
                pulse = eveningData.valueOf(DataType.PULSE),
                cellColor = eveningDataCommentColor,
                fontSize = settingsState.fontSize,
                onCellClick = {
                    onCellClick(index, DayPart.EVENING)
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = monthState.year,
                            month = monthState.month,
                            day = index,
                            wroteAt = DayPart.EVENING
                        )
                    )
                }
            )
        }
    }
}

fun getCellColor(data: List<PressureData>?, defaultColor: Int): Int {
    return data.colorValue() ?: defaultColor
}

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    upperPressure: String,
    lowerPressure: String,
    pulse: String,
    cellColor: Int,
    fontSize: Int,
    onCellClick: () -> Unit,
    onCellLongClick: () -> Unit
) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(cellColor)
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onCellClick,
                    onLongClick = onCellLongClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = upperPressure,
                    fontSize = fontSize.sp
                )
                Text(
                    modifier = Modifier,
                    text = "/",
                    fontSize = fontSize.sp
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = lowerPressure,
                    fontSize = fontSize.sp
                )
                Text(
                    modifier = Modifier,
                    text = " ",
                    fontSize = fontSize.sp
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = pulse,
                    fontSize = fontSize.sp
                )
            }
        }
    }
}

@Composable
fun Dropdown(
    options: List<String>,
    selected: String,
    width: Int = 152,
    height: Int = 56,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(),
            shape = RoundedCornerShape(12.dp),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}