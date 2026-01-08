@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria

import android.os.Bundle
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
import com.klyschenko.tonometria.presentation.screen.month.DayRow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var repository: RecordsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        lifecycleScope.launch {
//
//            val record = Record(
//                recordId = 1,
//                day = 7,
//                month = 1,
//                year = 2026,
//                upperPressure = 120,
//                lowerPressure = 80,
//                pulse = 65,
//                wroteAt = DayPart.DAY,
//                comment = "Works!"
//            )
//
//            val record2 = Record(
//                recordId = 2,
//                day = 7,
//                month = 1,
//                year = 2026,
//                upperPressure = 121,
//                lowerPressure = 79,
//                pulse = 77,
//                wroteAt = DayPart.DAY,
//                comment = "Oo!"
//            )
//
//            repository.addNewRecord(record)
//            repository.editRecord(1, toUpdate = ToUpdate.Comment("Cool"))
//            repository.addNewRecord(record2)
//
//            lifecycleScope.launch {
//                repository.getAllMonthRecords(2026, 1)
//                    .collect { records ->
//                        Log.d("Debug", "size=${records.size} records=$records")
//                    }
//            }
//        }

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
