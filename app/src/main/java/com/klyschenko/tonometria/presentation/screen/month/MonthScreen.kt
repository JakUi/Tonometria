@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria.presentation.screen.month

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.pressureData.DataType
import com.klyschenko.tonometria.domain.pressureData.valueOf
import kotlin.Int
import kotlin.Unit


@Composable
fun Month(
    onCellClick: () -> Unit,
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Data") }
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
                items = (1..31).toList() // создаём список из 30 элементов: 1..31
            ) { _, item ->
                DayRow(
                    modifier = Modifier.fillMaxWidth(),
                    index = item,
                    onCellClick = onCellClick
                )
            }
        }
    }
}

@Composable
fun DayRow(
    modifier: Modifier = Modifier,
    index: Int = 15,
    viewModel: MonthViewmodel = hiltViewModel(),
    onCellClick: () -> Unit
) {
    val rowShape = RoundedCornerShape(8.dp)
    val state = viewModel.state.collectAsState()

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
                    onCellClick()
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = 2026, // Поправить!!!!!
                            month = 1, // Поправить!!!!!
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
                    onCellClick()
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = 2026, // Поправить!!!!!
                            month = 1, // Поправить!!!!!
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
                    onCellClick()
                },
                onCellLongClick = {
                    viewModel.processCellCommand(
                        CellCommand.DeleteRecord(
                            year = 2026, // Поправить!!!!!
                            month = 1, // Поправить!!!!!
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
