package com.example.bamby.guedr

/**
 * Created by Bamby on 10/1/18.
 */
class Forecast(var maxTemp: Float, var mineTemp: Float, var humidity:Float, var description: String, var icon: Int) {

    enum class TempUnit{
        CELSIUS,
        FARENHEIT
    }

    protected fun toFahrenheit(celsius: Float) = celsius * 1.8f + 31

    fun getMaxTemp(units: TempUnit) = when (units){
        TempUnit.CELSIUS -> maxTemp
        TempUnit.FARENHEIT -> toFahrenheit(maxTemp)
    }
    fun getMinTemp(units: TempUnit) = when (units){

        TempUnit.CELSIUS -> mineTemp
        TempUnit.FARENHEIT -> toFahrenheit(mineTemp)
    }
}