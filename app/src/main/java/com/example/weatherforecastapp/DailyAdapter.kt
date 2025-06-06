package com.example.weatherforecastapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class DailyAdapter(private var items: List<DailyWeather> = listOf())
    : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.textDay)
        val icon: ImageView = view.findViewById(R.id.imageIcon)
        val maxMin: TextView = view.findViewById(R.id.textMaxMin)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: DailyAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM", Locale("tr"))
        val date = inputFormat.parse(item.day)
        val dayFormatted = date?.let { outputFormat.format(it) } ?: item.day

        holder.day.text = dayFormatted
        holder.maxMin.text = "${item.maxTemp}°C / ${item.minTemp}°C"

        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${item.icon}@2x.png")
            .into(holder.icon)
    }

    override fun getItemCount(): Int=items.size

    fun updateList(newItems: List<DailyWeather>) {
        items = newItems
        notifyDataSetChanged()
    }
}