package com.example.bamby.guedr.adapter

import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.bamby.guedr.PREFERENCE_SHOW_CELSIUS
import com.example.bamby.guedr.R
import com.example.bamby.guedr.model.Forecast
import kotlinx.android.synthetic.main.content_forecast.view.*

class ForecastRecyclerViewAdapter(val forecast: List<Forecast>?) : RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder>() {

    var onClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.content_forecast, parent, false)
        view.setOnClickListener(onClickListener)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder?, position: Int) {
        if (forecast != null)
        {
            holder?.bindForecast(forecast[position], position)
        }
    }


    override fun getItemCount() = forecast?.size ?: 0

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val day = itemView.findViewById<TextView>(R.id.day)
        val forecastImage = itemView.findViewById<ImageView>(R.id.forecast_image)
        val maxTemp = itemView.findViewById<TextView>(R.id.max_temp)
        val minTemp = itemView.findViewById<TextView>(R.id.min_temp)
        val humidity = itemView.findViewById<TextView>(R.id.humidity)
        val forecastDescription = itemView.findViewById<TextView>(R.id.forecast_description)

        fun bindForecast(forecast: Forecast, position: Int) {
            // Accedemos al contexto
            val context = itemView.context

            // Actualizamos la vista con el modelo
            forecastImage.setImageResource(forecast.icon)
            forecastDescription.text = forecast.description
            updateTemperature(forecast, temperatureUnits())
            val humidityString = context.getString(R.string.humidity_format, forecast.humidity)
            humidity.text = humidityString
            day.text = generateDayText(position)
        }

        private fun generateDayText(position: Int) = when(position) {
            0 -> "Hoy"
            1 -> "Mañana"
            2 -> "Pasado mañana"
            3 -> "En 3 días"
            4 -> "En 4 días"
            5 -> "En 5 días"
            else -> "En 6 días"
        }

        private fun updateTemperature(forecast: Forecast, units: Forecast.TempUnit) {
            val unitsString = temperatureUnitsString(units)
            val maxTempString = itemView.context.getString(R.string.max_temp_format, forecast.getMaxTemp(units), unitsString)
            val minTempString = itemView.context.getString(R.string.min_temp_format, forecast.getMinTemp(units), unitsString)
            maxTemp.text = maxTempString
            minTemp.text = minTempString
        }

        private fun temperatureUnitsString(units: Forecast.TempUnit) = when (units) {
            Forecast.TempUnit.CELSIUS -> "ºC"
            else -> "F"
        }

        private fun temperatureUnits() = if (PreferenceManager.getDefaultSharedPreferences(itemView.context)
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true)) {
            Forecast.TempUnit.CELSIUS
        }
        else {
            Forecast.TempUnit.FARENHEIT
        }

    }


}