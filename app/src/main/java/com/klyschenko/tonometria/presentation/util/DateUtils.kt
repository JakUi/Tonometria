package com.klyschenko.tonometria.presentation.util

import com.klyschenko.tonometria.R
import com.klyschenko.tonometria.presentation.mapper.UiText

fun Int.getMonthName(): UiText.Res {

    val months = mapOf(
        1 to UiText.Res(R.string.january),
        2 to UiText.Res(R.string.february),
        3 to UiText.Res(R.string.march),
        4 to UiText.Res(R.string.april),
        5 to UiText.Res(R.string.may),
        6 to UiText.Res(R.string.june),
        7 to UiText.Res(R.string.july),
        8 to UiText.Res(R.string.august),
        9 to UiText.Res(R.string.september),
        10 to UiText.Res(R.string.october),
        11 to UiText.Res(R.string.november),
        12 to UiText.Res(R.string.december)
    )
    return months[this] ?: UiText.Res(R.string.january)
}

fun String.getMonthNumber(): Int {
    val months = mapOf(
        "January" to 1,
        "February" to 2,
        "March" to 3,
        "April" to 4,
        "May" to 5,
        "June" to 6,
        "July" to 7,
        "August" to 8,
        "September" to 9,
        "October" to 10,
        "November" to 11,
        "December" to 12,
        "Январь" to 1,
        "Февраль" to 2,
        "Март" to 3,
        "Апрель" to 4,
        "Май" to 5,
        "Июнь" to 6,
        "Июль" to 7,
        "Август" to 8,
        "Сентябрь" to 9,
        "Октябрь" to 10,
        "Ноябрь" to 11,
        "Декабрь" to 12,
    )
    return months[this] ?: 1
}

fun Int.getYearAsString(): String {
    return this.toString()
}