package com.example.bamby.guedr.model

import java.io.Serializable


data class City(var name:String, var forecast: Forecast?):Serializable {//constructor por defecto

    constructor(name: String) : this(name,null)//constructor opcional,
    override fun toString() = name
}