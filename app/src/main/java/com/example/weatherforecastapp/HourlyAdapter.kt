package com.example.weatherforecastapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HourlyAdapter(private val items: List<HourlyWeather>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.textTime)
        val temp: TextView = view.findViewById(R.id.textTemp)
        val icon: ImageView = view.findViewById(R.id.imageIcon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val item = items[position]
        holder.time.text = item.time
        holder.temp.text = "${item.temp}°C"
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${item.icon}@2x.png")
            .into(holder.icon)
    }

    override fun getItemCount(): Int=items.size


}