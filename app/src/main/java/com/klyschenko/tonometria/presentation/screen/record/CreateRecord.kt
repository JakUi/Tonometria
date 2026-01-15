@file:OptIn(ExperimentalMaterial3Api::class)

package com.klyschenko.tonometria.presentation.screen.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.klyschenko.tonometria.presentation.ui.textfield.DigitOnlyInputTransformation

@Composable
fun CreateRecord(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Add data") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
        ) {
            Field(text= "Upper")
            Field(text= "Lower")
            Field(text= "Pulse")
        }
    }
}

@Composable
fun Field(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(56.dp)
        ) {
            Text(text = "$text:")
        }
        Box(
            modifier = Modifier.width(240.dp)
        ) {
            TextField(
                state = rememberTextFieldState(),
                inputTransformation = InputTransformation.maxLength(3)
                    .then(DigitOnlyInputTransformation()),
                keyboardOptions = KeyboardOptions( // отображать клавиатуру только с цифрами
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

