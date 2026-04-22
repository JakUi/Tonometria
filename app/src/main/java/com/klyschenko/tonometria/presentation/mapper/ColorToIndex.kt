package com.klyschenko.tonometria.presentation.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class ColorIndex(val colorInInt: Int) {
    FIRST(colorInInt = Color(0xFFFDD9B5).toArgb()),
//    SECOND(colorInInt = Color(0xFFFBCEB1).toArgb()),
    SECOND(colorInInt = Color(0xFFFF0033).toArgb()),
    THIRD(colorInInt = Color(0xFF78DBE2).toArgb()),
    FOURTH(colorInInt = Color(0xFF42AAFF).toArgb()),
    FIFTH(colorInInt = Color(0xFFFFDB58).toArgb()),
//    SIXTH(colorInInt = Color(0xFFFF0033).toArgb())
}
