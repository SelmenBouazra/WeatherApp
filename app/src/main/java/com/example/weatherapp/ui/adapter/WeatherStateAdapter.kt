package com.example.weatherapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.WeatherSateItemBinding
import com.example.weatherapp.model.DataList
import com.example.weatherapp.utils.toCelsius
import com.example.weatherapp.utils.toTime
import com.example.weatherapp.utils.weatherStateIcon

class WeatherStateAdapter : RecyclerView.Adapter<WeatherStateAdapter.ViewHolder>() {
    private var appList : List<DataList> = emptyList()

    class ViewHolder(var binding: WeatherSateItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WeatherSateItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appList[position]
        holder.binding.weatherTime.text = item.dt_txt.toTime()
        holder.binding.weatherState.setImageResource(weatherStateIcon[item.weather.first().id]!!)
        (item.main.temp.toCelsius()+"°C").also { holder.binding.weatherTemperature.text = it }
    }

    override fun getItemCount() = appList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList : List<DataList>){
        appList = newList
        notifyDataSetChanged()
    }
}