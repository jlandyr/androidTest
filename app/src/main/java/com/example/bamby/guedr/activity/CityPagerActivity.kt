package com.example.bamby.guedr.activity

import android.app.Fragment
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import com.example.bamby.guedr.R
import com.example.bamby.guedr.fragment.ForecastFragment
import com.example.bamby.guedr.model.Cities

class CityPagerActivity : AppCompatActivity() {

    val pager by lazy { findViewById<ViewPager>(R.id.view_pager)}
    val cities = Cities()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_pager)

        //configuramos toolbar
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)//toolbar hace de actionBar

//        val pager = findViewById<ViewPager>(R.id.view_pager)

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
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateCityInfo(position)
            }

        })

        updateCityInfo(0)
    }

    private fun updateCityInfo(position: Int) {
        supportActionBar?.title = cities[position].name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.pager, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.previous -> {
                pager.currentItem = pager.currentItem - 1
                true
            }
            R.id.next -> {
                pager.currentItem++
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        invalidateOptionsMenu()

        val menuPrev = menu?.findItem(R.id.previous)
        menuPrev?.setEnabled(pager.currentItem > 0)

        val menuNext = menu?.findItem(R.id.next)
        menuNext?.setEnabled(pager.currentItem < cities.count - 1)

        return true
    }
}