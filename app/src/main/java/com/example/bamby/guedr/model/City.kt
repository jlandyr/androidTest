package com.example.bamby.guedr.model

import java.io.Serializable


data class City(var name:String, var forecast: Forecast):Serializable {
    override fun toString(): String {
        return name
    }
}