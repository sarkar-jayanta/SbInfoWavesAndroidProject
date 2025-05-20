package com.example.mytestproject.ui

import android.util.Log
import androidx.activity.viewModels
import com.example.mytestproject.R
import com.example.mytestproject.adapter.TimeSlotAdapter
import com.example.mytestproject.databinding.ActivityMainBinding
import com.example.mytestproject.model.TimeSlotGenerator
import com.example.mytestproject.vm.MainViewModel
import com.sit.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    override fun init() {
        viewModel.fetchTimings()
        observer()

    }
    private fun observer(){
        viewModel.timingsList.observe(this){
            Log.d("timingsList", it.toString())
            val slotData = TimeSlotGenerator.generateSlots(it)
            val adapter = TimeSlotAdapter(slotData)
            binding.rvTimings.adapter = adapter
        }
    }

}