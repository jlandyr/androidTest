package com.example.bamby.guedr.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.bamby.guedr.R
import com.example.bamby.guedr.fragment.CityListFragment
import com.example.bamby.guedr.model.Cities
import com.example.bamby.guedr.model.City
import kotlinx.android.synthetic.main.activity_forecast.*

class ForecastActivity : AppCompatActivity(), CityListFragment.OnCitySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        //Comprobamos que el fragment no hay sido agregado a la jerarquía anteriormente
        if (fragmentManager.findFragmentById(R.id.city_list_fragment) == null){
            val fragment = CityListFragment.newInstance(Cities())
            fragmentManager.beginTransaction()
                    .add(R.id.city_list_fragment,fragment)
                    .commit()
        }

    }

    override fun onCitySelected(city: City?, position: Int) {
        startActivity(CityPagerActivity.intent(this, position))
    }

}



