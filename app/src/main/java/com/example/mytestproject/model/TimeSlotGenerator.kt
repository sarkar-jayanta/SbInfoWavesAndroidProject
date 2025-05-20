package com.example.mytestproject.model

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimeSlotGenerator {

    private val dayMap = mapOf(
        "Sunday" to Calendar.SUNDAY,
        "Monday" to Calendar.MONDAY,
        "Tuesday" to Calendar.TUESDAY,
        "Wednesday" to Calendar.WEDNESDAY,
        "Thursday" to Calendar.THURSDAY,
        "Friday" to Calendar.FRIDAY,
        "Saturday" to Calendar.SATURDAY
    )

    private val timeFormatter = SimpleDateFormat("HH:mm:ss z", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("GMT+05:30")
    }

    private val outputFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    private val dateFormatter = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)

    fun generateSlots(availabilityList: List<DayTimings>): List<TimeSlotData> {
        val result = mutableListOf<TimeSlotData>()
        val now = Calendar.getInstance()

        var checkedDays = 0
        var offset = 0

        while (result.size < 7 && checkedDays < 30) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, offset)
            }
            offset++
            checkedDays++

            val dow = cal.get(Calendar.DAY_OF_WEEK)

            val dayData = availabilityList.firstOrNull {
                dayMap[it.day] == dow && it.is_open
            } ?: continue

            val start = timeFormatter.parse(dayData.start_at)
            val end = timeFormatter.parse(dayData.end_at)

            val startCal = Calendar.getInstance().apply {
                time = start
                set(Calendar.YEAR, cal.get(Calendar.YEAR))
                set(Calendar.MONTH, cal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
            }

            val endCal = Calendar.getInstance().apply {
                time = end
                set(Calendar.YEAR, cal.get(Calendar.YEAR))
                set(Calendar.MONTH, cal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
            }

            val firstSlot = roundUpToNext30(startCal)

            if (isSameDay(cal, now)) {
                val oneHourLater = Calendar.getInstance().apply {
                    add(Calendar.HOUR_OF_DAY, 1)
                }
                if (firstSlot.before(oneHourLater)) {
                    firstSlot.timeInMillis = oneHourLater.timeInMillis
                    roundUpToNext30(firstSlot)
                }
            }

            val lastSlot = (endCal.clone() as Calendar).apply {
                add(Calendar.MINUTE, -30)
            }

            val slots = mutableListOf<String>()
            val cursor = firstSlot.clone() as Calendar

            while (!cursor.after(lastSlot)) {
                slots.add(outputFormatter.format(cursor.time))
                cursor.add(Calendar.MINUTE, 30)
            }

            if (slots.isNotEmpty()) {
                result.add(TimeSlotData(dateFormatter.format(cal.time), slots))
            }
        }

        return result
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun roundUpToNext30(cal: Calendar): Calendar {
        val min = cal.get(Calendar.MINUTE)
        return cal.apply {
            when {
                min == 0 || min == 30 -> {}
                min < 30 -> set(Calendar.MINUTE, 30)
                else -> {
                    set(Calendar.MINUTE, 0)
                    add(Calendar.HOUR_OF_DAY, 1)
                }
            }
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
}
