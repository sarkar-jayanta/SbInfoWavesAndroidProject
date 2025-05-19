package com.example.mytestproject.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.boilerplate.base.BaseActivity
import com.example.mytestproject.R
import com.example.mytestproject.adapter.TimeSlotAdapter
import com.example.mytestproject.databinding.ActivityMainBinding
import com.example.mytestproject.model.TimeSlotGenerator
import com.example.mytestproject.vm.MainViewModel

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
            val slotData = TimeSlotGenerator.generateSlots(it.toString())
            val adapter = TimeSlotAdapter(slotData)
            binding.rvTimings.adapter = adapter
        }
    }

}