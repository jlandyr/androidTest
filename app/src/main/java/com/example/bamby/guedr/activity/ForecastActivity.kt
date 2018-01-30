package com.example.bamby.guedr.activity

import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.bamby.guedr.R
import com.example.bamby.guedr.fragment.CityListFragment
import com.example.bamby.guedr.fragment.CityPagerFragment
import com.example.bamby.guedr.model.Cities
import com.example.bamby.guedr.model.City
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        //obtener info del dispositivo
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val dpWidth = (width / metrics.density)
        val dpHeight = (height / metrics.density)
        val androidVersion = Build.VERSION.SDK_INT
        val model = Build.MODEL

        // Comprobamos que en la interfaz tenemos un FrameLayout llamado city_list_fragment
        if (findViewById<View>(R.id.city_list_fragment) != null) {
            // Comprobamos primero que no tenemos ya añadido el fragment a nuestra jerarquía
            if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null) {
                val fragment = CityListFragment.newInstance()
                fragmentManager.beginTransaction()
                        .add(R.id.city_list_fragment, fragment)
                        .commit()
            }
        }

        // Hacemos lo mismo pero con el fragment de CityPagerFragment
        if (findViewById<View>(R.id.fragment_city_pager) != null) {
            if (fragmentManager.findFragmentById(R.id.fragment_city_pager) == null) {
                val fragment = CityPagerFragment.newInstance(0)
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_city_pager, fragment)
                        .commit()
            }
        }

        findViewById<FloatingActionButton?>(R.id.add_city_button)?.setOnClickListener { v: View ->
            Snackbar.make(
                    v,
                    "Aquí implementaríamos cosas interesantes",
                    Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    override fun onCitySelected(city: City?, position: Int) {
        val cityPagerFragment = fragmentManager.findFragmentById(R.id.fragment_city_pager) as? CityPagerFragment
        if (cityPagerFragment == null) {
            startActivity(CityPagerActivity.intent(this, position))
        }else{
            //tenemos el viewPager, y debe moverse
            cityPagerFragment.moveToCity(position)
        }

    }


}



