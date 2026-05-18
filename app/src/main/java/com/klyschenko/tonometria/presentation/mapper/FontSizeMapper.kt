package com.klyschenko.tonometria.presentation.mapper


fun mapPercentToFontSize(percent: String): Int {
    return when (percent) {
        "70%" -> 9
        "80%" -> 10
        "90%" -> 11
        "100%" -> 12
        "110%" -> 13
        "120%" -> 14
        "130%" -> 15
        else -> 12
    }
}

fun fontSizeToPercent(fontSize: Int): String {
    return when (fontSize) {
        9 -> "70%"
        10 -> "80%"
        11 -> "90%"
        12 -> "100%"
        13 -> "110%"
        14 -> "120%"
        15 -> "130%"
        else -> "100%"
    }
}