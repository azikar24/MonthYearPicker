package com.azikar24.monthyearpicker


import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import java.util.*

fun Calendar.monthDiff(secondCalendar: Calendar): Long {
    val date1 = LocalDate.of(get(Calendar.YEAR), get(Calendar.MONTH) + 1, 1)
    val date2 = LocalDate.of(secondCalendar.get(Calendar.YEAR), secondCalendar.get(Calendar.MONTH) + 1, 1)
    return (ChronoUnit.MONTHS.between(date1, date2))
}

fun Calendar.yearDiff(secondCalendar: Calendar): Long {
    val date1 = LocalDate.of(get(Calendar.YEAR), 1, 1)
    val date2 = LocalDate.of(secondCalendar.get(Calendar.YEAR), 1, 1)
    return (ChronoUnit.YEARS.between(date1, date2))
}

fun Date.toCalendar(): Calendar {
    val dateTime = this
    return Calendar.getInstance().apply {
        time = dateTime
    }
}

