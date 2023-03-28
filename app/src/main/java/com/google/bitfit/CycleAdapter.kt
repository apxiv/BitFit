package com.google.bitfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CycleAdapter(private val cycleList: List<CycleEntity>): RecyclerView.Adapter<CycleAdapter.ViewHolder>() {

    private val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = cycleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val numDays = cycleList[position].endPeriod.time - cycleList[position].startPeriod.time
            val diffInDays = TimeUnit.MILLISECONDS.toDays(numDays) + 1
            findViewById<TextView>(R.id.startDate).text = formatter.format(cycleList[position].startPeriod)
            //findViewById<TextView>(R.id.startDate).text = cycleList[position].startPeriod.toString()
            findViewById<TextView>(R.id.days).text = diffInDays.toString()
        }
    }
}