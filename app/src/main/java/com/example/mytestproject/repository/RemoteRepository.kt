package com.example.mytestproject.repository

import com.example.mytestproject.model.DayTimings
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
fun reqTimeslot () : Flow<ArrayList<DayTimings>>
}