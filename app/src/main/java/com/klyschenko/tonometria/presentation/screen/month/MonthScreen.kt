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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.klyschenko.tonometria.R
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.pressureData.DataType
import com.klyschenko.tonometria.domain.pressureData.valueOf
import com.klyschenko.tonometria.presentation.util.getMonthName
import com.klyschenko.tonometria.presentation.util.getMonthNumber
import com.klyschenko.tonometria.presentation.util.getYearAsString
import kotlin.Int
import kotlin.Unit


@Composable
fun Month(
    viewModel: MonthViewmodel = hiltViewModel(),
    onCellClick: (day: Int, dayPart: DayPart) -> Unit,
    onYearClick: () -> Unit
) {
    val dateState by viewModel.dateState.collectAsState()
    val dateLoadedState by viewModel.dateLoaded.collectAsState()

    if (!dateLoadedState) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                model = R.drawable.loader,
                contentDescription = "Image from gallery",
                contentScale = ContentScale.FillWidth
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
                        MonthDropdown(
                            options = listOf(
                                "January",
                                "February",
                                "March",
                                "April",
                                "May",
                                "June",
                                "July",
                                "August",
                                "September",
                                "October",
                                "November",
                                "December"
                            ),
                            selected = dateState.month.getMonthName(),
                            onSelected = {
                                viewModel.processDateCommand(command = DateCommand.ChangeMonth(month = it.getMonthNumber()))
                            }
                        )
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    top = 24.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = (1 .. viewModel.daysInMonth(dateState.month)).toList()
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
    val monthState by viewModel.dateState.collectAsState()

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
            Cell(
                modifier = Modifier.weight(1f),
                upperPressure = morningData.valueOf(DataType.UPPER),
                lowerPressure = morningData.valueOf(DataType.LOWER),
                pulse = morningData.valueOf(DataType.PULSE),
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

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    upperPressure: String,
    lowerPressure: String,
    pulse: String,
    onCellClick: () -> Unit,
    onCellLongClick: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier,
                    text = "/",
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = lowerPressure,
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier,
                    text = " ",
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = pulse,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun MonthDropdown(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .width(152.dp)
            .height(56.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 20.sp
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