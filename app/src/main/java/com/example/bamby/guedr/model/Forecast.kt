package com.example.bamby.guedr.model

import java.io.Serializable

class Forecast(var maxTemp: Float, var mineTemp: Float, var humidity:Float, var description: String, var icon: Int) : Serializable {

    enum class TempUnit{
        CELSIUS,
        FARENHEIT
    }

    protected fun toFahrenheit(celsius: Float) = celsius * 1.8f + 32

    init {
//        if (humidity < 0  || humidity > 100){
//            throw IllegalArgumentException("Humidity should be between 0 and 100")
//        }
        if (humidity !in 0f..100f){
            throw IllegalArgumentException("Humidity should be between 0 and 100")
        }
    }
    fun getMaxTemp(units: TempUnit) = when (units){
        TempUnit.CELSIUS -> maxTemp
        TempUnit.FARENHEIT -> toFahrenheit(maxTemp)
    }
    fun getMinTemp(units: TempUnit) = when (units){

        TempUnit.CELSIUS -> mineTemp
        TempUnit.FARENHEIT -> toFahrenheit(mineTemp)
    }
}