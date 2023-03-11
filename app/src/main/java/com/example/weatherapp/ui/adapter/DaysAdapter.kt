package com.example.weatherapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.DaysItemBinding
import com.example.weatherapp.model.WeatherInfo
import com.example.weatherapp.utils.toDay
import com.example.weatherapp.utils.weatherStateIcon

class DaysAdapter : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
    private var daysWeatherInfo :  List<WeatherInfo>  = emptyList()
    var onItemClick : ((String)->Unit)? = null

    class ViewHolder(var binding: DaysItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DaysItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = daysWeatherInfo[position]
        holder.binding.day.text = item.day.toDay()
        holder.binding.weatherState.setImageResource(weatherStateIcon[item.icon]!!)
        holder.binding.weatherDescription.text = item.description
        holder.binding.weatherTemperatureRange.text = item.temperature

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item.day)
        }
    }

    override fun getItemCount() = daysWeatherInfo.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList : List<WeatherInfo>){
        daysWeatherInfo = newList
        notifyDataSetChanged()
    }
}