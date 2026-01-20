package com.klyschenko.tonometria.domain.entity

enum class DayPart(val partName: String) {

    MORNING(partName = "MORNING"),
    DAY(partName = "DAY"),
    EVENING(partName = "EVENING");

    companion object {
        private val map = entries.associateBy(DayPart::partName)

        fun getByName(name: String?): DayPart? {
            return map[name]
        }
    }
}