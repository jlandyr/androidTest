package com.example.bamby.guedr.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView

import com.example.bamby.guedr.PREFERENCE_SHOW_CELSIUS
import com.example.bamby.guedr.R
import com.example.bamby.guedr.activity.SettingsActivity
import com.example.bamby.guedr.model.City
import com.example.bamby.guedr.model.Forecast
import org.w3c.dom.Text

class ForecastFragment: Fragment() {
    companion object {
        val REQUEST_UNITS = 1
        private var ARG_CITY = "ARG_CITY"

        fun newInstance(city:City): ForecastFragment {
            val fragment = ForecastFragment()
            val arguments = Bundle()
            arguments.putSerializable(ARG_CITY, city)
            fragment.arguments = arguments
            return fragment
        }
    }

    lateinit var root: View
    lateinit var maxTemp: TextView //lateinit asegura que al momento de crear la variable va a tener una valor
    lateinit var minTemp: TextView  //lateinit permite usarla sin ? al final del nombre del a variable


    var city: City? = null
        set(value){
            if (value != null){
                root.findViewById<TextView>(R.id.city).text = value.name
                forecast = value.forecast
            }
        }

    var forecast : Forecast? = null
        set(value) {
            field = value
            val forecastImage = root.findViewById<ImageView>(R.id.forecast_image)
            maxTemp = root.findViewById(R.id.max_temp)
            minTemp = root.findViewById(R.id.min_temp)
            val humidity = root.findViewById<TextView>(R.id.humidity)
            val forecastDescription = root.findViewById<TextView>(R.id.forecast_description)

            //actualizamos vista con modelo
            value?.let{//parecido a if let de swift
                forecastImage.setImageResource(value.icon)
                forecastDescription.text = value.description
                updateTemperature()
                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
         super.onCreateView(inflater, container, savedInstanceState)
        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_forecast, container, false)
//            forecast = Forecast(maxTemp = 25f, mineTemp = 10f, humidity = 35f, description = "Día soleado", icon = R.drawable.ico_01)
            if (arguments != null){
                city = arguments.getSerializable(ARG_CITY) as? City
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_show_settings){
            val units =  if (temperatureUnits() == Forecast.TempUnit.CELSIUS)
                R.id.celsius_rb
            else
                R.id.farenheit_rb
            //aquí sabemos que item se seleccionó
            val intent = SettingsActivity.intent(activity,units )
            //startActivity(intent)//la 2da pantalla no nos devuelve ningún valor
            startActivityForResult(intent, REQUEST_UNITS)//cuando la pantalla q llamamos nos va a devolver algún valor
            return true
        }
        return super.onOptionsItemSelected(item)

    }
    private fun updateTemperature() {
        val units = temperatureUnits()
        val unitsString = temperatureUnitsString(units)
        val maxTempString = getString(R.string.max_temp_format, forecast?.getMaxTemp(units), unitsString)
        val minTempString = getString(R.string.min_temp_format, forecast?.getMinTemp(units), unitsString)
        maxTemp.text = maxTempString
        minTemp.text = minTempString
    }
    private fun temperatureUnitsString(units: Forecast.TempUnit) = if (units == Forecast.TempUnit.CELSIUS) "C" else "F"


    private fun temperatureUnits(): Forecast.TempUnit {
        return when (PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true)){
            true -> Forecast.TempUnit.CELSIUS
            false -> Forecast.TempUnit.FARENHEIT
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val unitSelected = data?.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb)
                if (unitSelected == R.id.celsius_rb){
                    Log.v("TAG","Soy ForecastActivity y han pulsado OK y celsius_rb")
//                    Toast.makeText(this, "Celsius seleccionado", Toast.LENGTH_LONG).show()

                }else if (unitSelected == R.id.farenheit_rb){
                    Log.v("TAG","Soy ForecastActivity y han pulsado OK y fahrenheit_rb")
//                    Toast.makeText(this, "Fahrenheit seleccionado", Toast.LENGTH_SHORT).show()
                }

                val oldPreferences = temperatureUnits()

                PreferenceManager.getDefaultSharedPreferences(activity)
                        //equivalente a NSUserDefaults de Swift
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, unitSelected == R.id.celsius_rb)
                        .apply()
                updateTemperature()

                Snackbar.make(root, "Han cambiado las preferencias", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            PreferenceManager.getDefaultSharedPreferences(activity)
                                    //equivalente a NSUserDefaults de Swift
                                    .edit()
                                    .putBoolean(PREFERENCE_SHOW_CELSIUS, oldPreferences == Forecast.TempUnit.CELSIUS)
                                    .apply()
                            updateTemperature()
                        }
                        .show()

            }else{
                Log.v("TAG","Soy ForecastActivity y han pulsado CANCEL")
            }
        }
    }
}