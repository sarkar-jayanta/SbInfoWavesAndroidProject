package com.example.mytestproject

import com.example.mytestproject.model.DayTimings
import retrofit2.Response

import retrofit2.http.GET

interface ApiInterface {
    @GET("api/sample")
    suspend fun getTimeSlot() : Response<ArrayList<DayTimings>>
}