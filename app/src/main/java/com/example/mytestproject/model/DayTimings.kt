package com.example.mytestproject.model

data class DayTimings(
    val id: Int,
    val day: String,
    val start_at: String,
    val end_at: String,
    val is_open: Boolean
){
}

data class TimeSlotData(
    val date : String,
    val slots : List<String>
)