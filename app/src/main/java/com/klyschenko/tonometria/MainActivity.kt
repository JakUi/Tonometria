@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klyschenko.tonometria.presentation.ui.theme.TonometriaTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.lifecycleScope
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.domain.entity.PressureData
import com.klyschenko.tonometria.domain.repository.RecordsRepository
import com.klyschenko.tonometria.domain.repository.ToUpdate
import com.klyschenko.tonometria.presentation.screen.month.DayRow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: RecordsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {

            val record = com.klyschenko.tonometria.domain.entity.Record(
                day = 7,
                month = 1,
                year = 2026,
                wroteAt = DayPart.DAY,
                data = PressureData(120, 80, 67, "Added")
            )

            val record2 = com.klyschenko.tonometria.domain.entity.Record(
                day = 7,
                month = 1,
                year = 2026,
                wroteAt = DayPart.DAY,
                data = PressureData(118, 76, 64, "Second")
            )

            repository.addNewRecord(record)
            repository.editRecord(1, toUpdate = ToUpdate.Comment("Cool"))
            repository.addNewRecord(record2)

            lifecycleScope.launch {
                repository.getAllMonthRecords(2026, 1)
                    .collect { records ->
                        Log.d("Debug", "size=${records.size} records=$records")
                    }
            }
        }

        setContent {
            TonometriaTheme {
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
                            items = (1 .. 31).toList() // создаём список из 30 элементов: 1..31
                        ) { _, item ->
                            DayRow(
                                modifier = Modifier.fillMaxWidth(),
                                index = item
                            )
                        }
                    }
                }
            }
        }
    }
}
