package com.example.mytestproject.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytestproject.model.DayTimings
import com.example.mytestproject.repository.RemoteRepository
import com.sit.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository, @ApplicationContext val context : Context
)  : BaseViewModel(){

    val timingsList = MutableLiveData<ArrayList<DayTimings>>()

    fun fetchTimings(){
        viewModelScope.launch(Main){
            remoteRepository.reqTimeslot().flowOn(IO)
                .catch {
                    Log.d("error", "Something went wrong")
                }.collect{
                    timingsList.postValue(it)
                }
        }
    }
}