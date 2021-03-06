package com.example.bamby.guedr

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ForecastUnitTest {
    lateinit var forecast : Forecast

    @Before
    fun setUp(){
        forecast = Forecast(25f, 10f, 35f, "Soleado con alguna nube", R.drawable.ico_01)
    }

    @Test
    fun maxTempUnitsConversion_isCorrect(){
        assertEquals(77f, forecast.getMaxTemp(Forecast.TempUnit.FARENHEIT))
    }

    @Test
    fun minTempUnitsConversion_isCorrect(){
        assertEquals(50f, forecast.getMinTemp(Forecast.TempUnit.FARENHEIT))
    }

    @Test
    fun maxTempUnits_inCelsius(){
        assertEquals(25f, forecast.getMaxTemp(Forecast.TempUnit.CELSIUS))
    }

    @Test
    fun minTempUnits_inCelsius(){
        assertEquals(10f, forecast.getMinTemp(Forecast.TempUnit.CELSIUS))
    }

    @Test(expected = IllegalArgumentException::class)
    fun humidityOverRange_throwsArgumentException(){
        Forecast(25f, 10f, 100.01f, "Description", R.drawable.ico_01)
    }

    @Test(expected = IllegalArgumentException::class)
    fun humidityUnderRange_throwsArgumentException(){
        Forecast(25f, 10f, -1f, "Description", R.drawable.ico_01)
    }
}