package com.klyschenko.tonometria.presentation.screen.month

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Preview
@Composable
fun DayRow(
    modifier: Modifier = Modifier,
    index: Int = 15,
    viewModel: MonthViewmodel = hiltViewModel()
) {
    val rowShape = RoundedCornerShape(8.dp)
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
            repeat(3) {
                Card(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .weight(1f)
                        .height(56.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                ) {
                    val _state by viewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(_state) {
                        Log.d("Debug", "state=$_state")
                    }
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
                                text = "",
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
                                text = "",
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
                                text = "",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
