package com.klyschenko.tonometria.data.db.converters

import androidx.room.TypeConverter
import com.klyschenko.tonometria.domain.entity.DayPart

class DayPartConverter {
    @TypeConverter
    fun toString(dayPart: DayPart): String = dayPart.name

    @TypeConverter
    fun fromString(value: String): DayPart = DayPart.valueOf(value)
}