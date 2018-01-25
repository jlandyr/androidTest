package com.example.bamby.guedr.activity

import android.app.Fragment
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toolbar
import com.example.bamby.guedr.R
import com.example.bamby.guedr.fragment.ForecastFragment
import com.example.bamby.guedr.model.Cities

class CityPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pager)

        //configuramos toolbar
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)//toolbar hace de actionBar

        val pager = findViewById<ViewPager>(R.id.view_pager)
        val cities = Cities()
        val adapter = object : FragmentPagerAdapter(fragmentManager){
            override fun getItem(position: Int): Fragment {
                return ForecastFragment.newInstance(cities[position])
            }

            override fun getCount(): Int {
                return cities.count
            }

            override fun getPageTitle(position: Int): CharSequence {
                return cities[position].name
            }

        }

        pager.adapter = adapter
    }
}
