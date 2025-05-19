package com.example.mytestproject.model

import android.icu.util.Calendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimeSlotGenerator {
    private  val gson = Gson()
    private val dayMap = mapOf(
        "Sunday" to Calendar.SUNDAY,
        "Monday" to Calendar.MONDAY,
        "Tuesday" to Calendar.TUESDAY,
        "Wednesday" to Calendar.WEDNESDAY,
        "Thursday" to Calendar.THURSDAY,
        "Friday" to Calendar.FRIDAY,
        "saturday" to Calendar.SATURDAY,
    )

    private val timeFormatter = SimpleDateFormat("HH:mm:ss z", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("GMT+05:30")
    }

    private val outputFormatter = SimpleDateFormat("hh:mm:a", Locale.ENGLISH)
    private val dateFormatter = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)

    fun generateSlots(slots : String) : List<TimeSlotData>{
        val listType = object : TypeToken<List<DayTimings>>(){}.type
        val availabilityList : List<DayTimings> = gson.fromJson(slots, listType)
        val result = mutableListOf<TimeSlotData>()
        val now = Calendar.getInstance()

        for (i in 0 until 7){
            val cal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, i)
            }
            val dow = cal.get(Calendar.DAY_OF_WEEK)

            val dayData = availabilityList.firstOrNull{
                dayMap[it.day] == dow && it.is_open } ?: continue

            val start = timeFormatter.parse(dayData.start_at)
            val end = timeFormatter.parse(dayData.end_a)

            val startCal = Calendar.getInstance().apply {
                time = start
                set(Calendar.YEAR, cal.get(Calendar.YEAR))
                set(Calendar.MONDAY, cal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
            }

            val endCal = Calendar.getInstance().apply {
                time = end
                set(Calendar.YEAR, cal.get(Calendar.YEAR))
                set(Calendar.MONDAY, cal.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
            }
            val firstSlot = roundUpToNext30(startCal)

            if (i == 0){
                val oneHourlater = Calendar.getInstance().apply {
                    add(Calendar.HOUR_OF_DAY, 1)
                }
                if (firstSlot.before(oneHourlater)){
                    firstSlot.timeInMillis = oneHourlater.timeInMillis
                    roundUpToNext30(firstSlot)
                }
            }

            val lastSlot = (endCal.clone() as Calendar).apply {
                add(Calendar.MINUTE, -30)
            }
            val slots = mutableListOf<String>()
            val cursor = firstSlot.clone() as Calendar
            while (!cursor.after(lastSlot)){
                slots.add((outputFormatter.format(cursor.time)))
            }

            if (slots.isNotEmpty()){
                result.add(TimeSlotData(dateFormatter.format(cal.time), slots))
            }

        }
        return result
        }

    fun roundUpToNext30(cal: Calendar): Calendar{
        val min = cal.get(Calendar.MINUTE)
        return cal.apply {
            when {
                min == 0 || min == 30 -> {}
                min < 30 -> {
                    set(Calendar.MINUTE, 30)
                }
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
