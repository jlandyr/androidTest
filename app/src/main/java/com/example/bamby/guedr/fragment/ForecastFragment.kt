package com.example.bamby.guedr.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import com.example.bamby.guedr.CONSTANT_OWM_APIKEY

import com.example.bamby.guedr.PREFERENCE_SHOW_CELSIUS
import com.example.bamby.guedr.R
import com.example.bamby.guedr.activity.SettingsActivity
import com.example.bamby.guedr.model.City
import com.example.bamby.guedr.model.Forecast
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ForecastFragment: Fragment() {

    enum class VIEW_INDEX(val index:Int){
        LOADING(0),
        FORECAST(1)
    }
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
    lateinit var viewSwitcher: ViewSwitcher

    var city: City? = null
        set(value){
            field = value
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
            if (value != null){
                forecastImage.setImageResource(value.icon)
                forecastDescription.text = value.description
                updateTemperature()
                val humidityString = getString(R.string.humidity_format, value.humidity)
                humidity.text = humidityString
                viewSwitcher.displayedChild = VIEW_INDEX.FORECAST.index
                city?.forecast = value //supercaché de la muerte
            }else{
                updateForecast()
            }

        }

    private fun updateForecast(){

       async(UI){
           viewSwitcher.displayedChild = VIEW_INDEX.LOADING.index
           val newForecast: Deferred<Forecast?> = bg {
               downloadForecast(city)//lo hace en background
           }
           forecast = newForecast.await()//main thread
       }

    }

    fun downloadForecast(city:City?):Forecast?{
        try {
            //descargamos info de openweathermap
            val url = URL("https://api.openweathermap.org/data/2.5/forecast/daily?q=${city?.name}&lang=sp&units=metric&appid=${CONSTANT_OWM_APIKEY}")
            val jsonString = Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next()

            //Analizamos JSON descargado
            val jsonRoot = JSONObject(jsonString)
            var list = jsonRoot.getJSONArray("list")
            val today = list.getJSONObject(0)
            val maxToday = today.getJSONObject("temp").getDouble("max").toFloat()
            val minToday = today.getJSONObject("temp").getDouble("min").toFloat()
            val humidity = today.getDouble("humidity").toFloat()
            val description = today.getJSONArray("weather").getJSONObject(0).getString("description")
            var iconString = today.getJSONArray("weather").getJSONObject(0).getString("icon")
            //convertimos texto iconString -> drawable
            iconString = iconString.substring(0, iconString.length - 1)//le quitamos el último caracter (d/n)
            val iconInt = iconString.toInt()
            val iconResource = when (iconInt){
                2 -> R.drawable.ico_02
                3 -> R.drawable.ico_03
                4 -> R.drawable.ico_04
                9 -> R.drawable.ico_09
                10 -> R.drawable.ico_10
                11 -> R.drawable.ico_11
                13 -> R.drawable.ico_13
                50 -> R.drawable.ico_50
                else -> R.drawable.ico_01
            }

            return Forecast(maxToday, minToday, humidity, description, iconResource)
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
         super.onCreateView(inflater, container, savedInstanceState)
        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_forecast, container, false)

            viewSwitcher = root.findViewById(R.id.view_switcher)
            viewSwitcher.setInAnimation(activity, android.R.anim.fade_in)
            viewSwitcher.setOutAnimation(activity, android.R.anim.fade_out)

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

    //para saber si estando en un Viewpager debemos refrescar las unidades
    //parecido al viewWillAppear de los fragment
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && forecast != null){
            updateTemperature()
        }
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