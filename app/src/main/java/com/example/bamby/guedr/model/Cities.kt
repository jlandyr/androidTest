package com.example.bamby.guedr.model

import com.example.bamby.guedr.R
import java.io.Serializable
//cambia de class a object para hacerlo singleton
object Cities: Serializable {
    private var cities: List<City> = listOf(
            City("Madrid"),
            City("Jaén"),
            City("Quito")
    )

    val count
        get() = cities.size

    //    fun getCity(index: Int) = cities[index]
    operator fun get(i: Int) = cities[i]

    fun toArray() = cities.toTypedArray()
}
