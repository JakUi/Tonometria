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
import androidx.lifecycle.lifecycleScope
import androidx.room.PrimaryKey
import com.klyschenko.tonometria.data.RecordsDao
import com.klyschenko.tonometria.data.RecordsDatabase
import com.klyschenko.tonometria.data.RecordsDbModel
import com.klyschenko.tonometria.domain.entity.DayPart
import com.klyschenko.tonometria.presentation.screen.month.DayRow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Month
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var recordsDao: RecordsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {

//            @PrimaryKey val recordId: Int,
//            val year: Int,
//            val month: Month,
//            val day: Int,
//            val wroteAt: DayPart,
//            val upperPressure: Int,
//            val lowerPressure: Int,
//            val pulse: Int,
//            val comment: String

            val record = RecordsDbModel(
                1,
                2026,
                Month.JANUARY,
                7,
                DayPart.DAY,
                120,
                80,
                71,
                ""
            )

            recordsDao.addNewRecord(record)
        }

//        setContent {
//            TonometriaTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = {
//                        TopAppBar(
//                            title = { Text("Data") }
//                        )
//                    }
//                ) { innerPadding ->
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                        contentPadding = PaddingValues(
//                            top = 24.dp,
//                            start = 16.dp,
//                            end = 16.dp,
//                            bottom = 16.dp
//                        ),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        itemsIndexed(
//                            items = (1 .. 31).toList() // создаём список из 30 элементов: 1..31
//                        ) { _, item ->
//                            DayRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                index = item
//                            )
//                        }
//                    }
//                }
//            }
//        }
    }
}
