package com.klyschenko.tonometria.presentation.mapper

import android.content.Context

sealed interface UiText {
    data class Res(val id: Int, val args: List<Any> = emptyList()) : UiText
    data class Plain(val value: String) : UiText
}

fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.Plain -> value
        is UiText.Res -> context.getString(id, *args.toTypedArray())
    }
}