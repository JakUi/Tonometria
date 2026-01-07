package com.klyschenko.tonometria.domain.usecase

import android.util.Printer
import com.klyschenko.tonometria.domain.repository.RecordRepository
import java.time.Month
import javax.inject.Inject

class GetAllMonthsRecordsUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
    private val year: Int,
    private val month: Month
) {

    operator fun invoke() = recordRepository.getAllMonthRecords(year, month)
}