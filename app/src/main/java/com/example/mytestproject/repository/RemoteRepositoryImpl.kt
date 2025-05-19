package com.example.mytestproject.repository

import android.content.Context
import com.example.mytestproject.ApiInterface
import com.example.mytestproject.model.DayTimings
import com.shaligram.tripuratourism.utils.SafeApiRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(@ApplicationContext val context: Context, private val apiInterface: ApiInterface) : RemoteRepository {
    override fun reqTimeslot(): Flow<ArrayList<DayTimings>> {
       return flow{
           emit(SafeApiRequest(context).apiRequest {
               apiInterface.getTimeSlot()
           })
       }
    }
}