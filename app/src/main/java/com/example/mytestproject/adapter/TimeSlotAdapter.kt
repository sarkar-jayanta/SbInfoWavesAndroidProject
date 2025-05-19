package com.example.mytestproject.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mytestproject.R
import com.example.mytestproject.model.DayTimings
import com.example.mytestproject.model.TimeSlotData

class TimeSlotAdapter(private val items : List<TimeSlotData>): RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotAdapter.ViewHolder {
        val view = LayoutInflater.from (parent.context).inflate(R.layout.layout_timings, parent , false)
    return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotAdapter.ViewHolder, position: Int) {
        val data = items[position]
        holder.dateText.text = data.date
        holder.slotsContainer.removeAllViews()

        data.slots.forEach {
            val tv = TextView(holder.itemView.context).apply {
                text = it
                setPadding(24, 16, 24, 16)
                background = ContextCompat.getDrawable(context, R.drawable.slot_bg)
                setTextColor(Color.BLACK)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8,8,8,8)
                layoutParams = params
            }
            holder.slotsContainer.addView(tv)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val dateText : TextView = view.findViewById(R.id.dateText)
        val slotsContainer : LinearLayout = view.findViewById(R.id.slotsContainer)
    }
}