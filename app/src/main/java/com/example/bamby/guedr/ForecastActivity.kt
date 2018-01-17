package com.example.bamby.guedr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_settings.*

class ForecastActivity : AppCompatActivity() {

    var maxTemp: TextView? = null
    var minTemp: TextView? = null

    var forecast : Forecast? = null
        set(value) {
            field = value
            val forecastImage = findViewById<ImageView>(R.id.forecast_image)
            maxTemp = findViewById<TextView>(R.id.max_temp)
            minTemp = findViewById<TextView>(R.id.min_temp)
            val humidity = findViewById<TextView>(R.id.humidity)
            val forecastDescription = findViewById<TextView>(R.id.forecast_description)

            //actualizamos vista con modelo
            if (value != null){
                forecastImage.setImageResource(value.icon)
                forecastDescription.text = value.description
//                val maxTempString = getString(R.string.max_temp_format, value.maxTemp)
//                val minTempString = getString(R.string.min_temp_format, value.mineTemp)
//                val humidityString = getString(R.string.humidity_format, value.humidity)
//                maxTemp?.text = maxTempString
//                minTemp?.text = minTempString
                updateTemperature()
                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        forecast = Forecast(maxTemp = 25f, mineTemp = 10f, humidity = 35f,description = "Día soleado", icon = R.drawable.ico_01)


    }

    //define que opciones de menu tenemos
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }
    //define que se hace una vez seleccionada una opción del menú
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings){
            val units =  if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.farenheit_rb
            //aquí sabeos que item se seleccionó
            val intent = SettingsActivity.intent(this,units )
            //startActivity(intent)//la 2da pantalla no nos devuelve ningún valor
            startActivityForResult(
                    //cuando la pantalla q llamamos nos va a devolver algún valor
                    intent,
                    1
            )
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val unitSelected = data?.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb)
                if (unitSelected == R.id.celsius_rb){
                    Log.v("TAG","Soy ForecastActivity y han pulsado OK y celsius_rb")

                }else if (unitSelected == R.id.farenheit_rb){
                    Log.v("TAG","Soy ForecastActivity y han pulsado OK y farenheit_rb")
                }
                PreferenceManager.getDefaultSharedPreferences(this)
                        //equivalente a NSUserDefaults de Swift
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitSelected == R.id.celsius_rb)
                        .apply()
                updateTemperature()

            }else{
                Log.v("TAG","Soy ForecastActivity y han pulsado CANCEL")
            }
        }


    }

    private fun updateTemperature() {
        val units = temperatureUnits()
        val unitsString = temperatureUnitsString(units)
        val maxTempString = getString(R.string.max_temp_format, forecast?.getMaxTemp(units), unitsString)
        val minTempString = getString(R.string.min_temp_format, forecast?.getMinTemp(units), unitsString)
        maxTemp?.text = maxTempString
        minTemp?.text = minTempString
    }

    private fun temperatureUnitsString(units: Forecast.TempUnit) = if (units == Forecast.TempUnit.CELSIUS) "C" else "F"


    private fun temperatureUnits(): Forecast.TempUnit {
        return when (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true)){
            true -> Forecast.TempUnit.CELSIUS
            false -> Forecast.TempUnit.FARENHEIT
        }
    }
}



