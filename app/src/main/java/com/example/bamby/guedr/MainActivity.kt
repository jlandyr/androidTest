package com.example.bamby.guedr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.canonicalName
    var stone_button : Button? = null
    var donkey_button: Button? = null

    var offlineWeatherImage : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forecast = Forecast(maxTemp = 25f, mineTemp = 10f, humidity = 35f,description = "Día soleado", icon = R.drawable.ico_01)

        setForecast(forecast)
    }

    private fun setForecast(forecast: Forecast) {
        //accedemos a las vistas de la interfaz
        val forecastImage = findViewById<ImageView>(R.id.forecast_image)
        val maxTemp = findViewById<TextView>(R.id.max_temp)
        val minTemp = findViewById<TextView>(R.id.min_temp)
        val humidity = findViewById<TextView>(R.id.humidity)
        val forecastDescription = findViewById<TextView>(R.id.forecast_description)

        //actualizamos vista con modelo
        forecastImage.setImageResource(forecast.icon)
        forecastDescription.text = forecast.description
        val maxTempString = getString(R.string.max_temp_format, forecast.maxTemp)
        val minTempString = getString(R.string.min_temp_format, forecast.mineTemp)
        val humidityString = getString(R.string.humidity_format, forecast.humidity)
        maxTemp.text = maxTempString
        minTemp.text = minTempString
        humidity.text = humidityString

    }
}



