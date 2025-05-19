package com.example.mytestproject.model

import com.google.gson.annotations.SerializedName

data class DayTimings(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("day")val  day: String? = null,
@SerializedName("start_at")val start_at: String? =  null,
@SerializedName("end_at") val end_a : String? = null,
@SerializedName("is_open")val is_open : Boolean = true
){
}

data class TimeSlotData(
    val date : String,
    val slots : List<String>
)