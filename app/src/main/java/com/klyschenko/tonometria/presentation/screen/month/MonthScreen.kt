package com.klyschenko.tonometria.presentation.screen.month

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.pressureData.DataType
import com.klyschenko.tonometria.domain.pressureData.valueOf


@Preview
@Composable
fun DayRow(
    modifier: Modifier = Modifier,
    index: Int = 15,
    viewModel: MonthViewmodel = hiltViewModel()
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
//            Log.d("Data", "$index morning data ${getData(morningData, DataType.UPPER)}")
//            Log.d("Data", "$index day data ${getData(dayData, DataType.UPPER)}")
//            Log.d("Data", "$index evening data ${getData(eveningData, DataType.UPPER)}")
            Cell(
                upperPressure = morningData.valueOf(DataType.UPPER),
                lowerPressure = morningData.valueOf(DataType.LOWER),
                pulse = morningData.valueOf(DataType.PULSE)

            )
            Cell(
                upperPressure = dayData.valueOf(DataType.UPPER),
                lowerPressure = dayData.valueOf(DataType.LOWER),
                pulse = dayData.valueOf(DataType.PULSE)
            )
            Cell(
                upperPressure = eveningData.valueOf(DataType.UPPER),
                lowerPressure = eveningData.valueOf(DataType.LOWER),
                pulse = eveningData.valueOf(DataType.PULSE)
            )
        }
    }
}

//val morningData = state[12]?.get(DayPart.MORNING)
//val dayData = state[12]?.get(DayPart.DAY)
//val eveningData = state[12]?.get(DayPart.EVENING)

//@Composable
//fun Morning(
//    modifier: Modifier = Modifier,
//    upperPressure: String,
//    lowerPressure: String,
//    pulse: String
//) {
//    Cell(
//        modifier = modifier,
//        upperPressure = upperPressure,
//        lowerPressure = lowerPressure,
//        pulse = pulse
//    )
//}

//@Composable
//fun Cell(
//    modifier: Modifier = Modifier,
//    state: Map<Int, Map<DayPart, List<PressureData>>>
//) {
//
//    Card(
//        modifier = modifier,
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//    ) {
//
//        LaunchedEffect(state) {
//            Log.d("Debug", "state=${state.keys}")
//        }
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = Modifier
//                        .padding(4.dp),
//                    text = morningData?.get(0)?.upperPressure.toString(),
//                    fontSize = 12.sp
//                )
//                Text(
//                    modifier = Modifier,
//                    text = "/",
//                    fontSize = 12.sp
//                )
//                Text(
//                    modifier = Modifier
//                        .padding(4.dp),
//                    text = morningData?.get(0)?.lowerPressure.toString(),
//                    fontSize = 12.sp
//                )
//                Text(
//                    modifier = Modifier,
//                    text = "-",
//                    fontSize = 12.sp
//                )
//                Text(
//                    modifier = Modifier
//                        .padding(4.dp),
//                    text = morningData?.get(0)?.pulse.toString(),
//                    fontSize = 12.sp
//                )
//            }
//        }
//    }
//}

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    upperPressure: String,
    lowerPressure: String,
    pulse: String
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                    text = "-",
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
