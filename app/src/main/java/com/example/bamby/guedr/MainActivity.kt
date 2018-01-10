package com.example.bamby.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = MainActivity::class.java.canonicalName
    var stone_button : Button? = null
    var donkey_button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stone_button = findViewById<Button>(R.id.stone_button)
        donkey_button = findViewById<Button>(R.id.donkey_button)
        stone_button?.setOnClickListener(this)
        donkey_button?.setOnClickListener(this)

        //otra forma más kotlinera de hacer
//        findViewById<Button>(R.id.stone_button).setOnClickListener(this)
//        findViewById<Button>(R.id.donkey_button).setOnClickListener(this)


        Log.v(TAG, "onCreate")
        if (savedInstanceState != null){
            //Log.v(TAG, savedInstanceState.getString("clave"))
            Log.v(TAG, "savedInstanceState !=null y la clave es: ${savedInstanceState.getString("clave")}")
        }else{
            Log.v(TAG, "savedInstanceState == null")
        }

    }

    override fun onClick(p0: View?) {
        Log.v(TAG,"önClick")
//        if (p0 == stone_button){
//            Log.v(TAG,"onClick -> stone_button")
//        }else{
//            Log.v(TAG,"onClick -> donkey_button")
//        }

        if (p0 != null){
            if (p0.id == R.id.stone_button){
                Log.v(TAG,"onClick -> stone_button")
            }else if (p0.id == R.id.donkey_button){
                Log.v(TAG,"onClick -> donkey_button")
            }
        }

        when (p0?.id){
            R.id.stone_button -> {
                Log.v(TAG,"onClick -> stone_button")
            }
            R.id.donkey_button -> {
                Log.v(TAG,"onClick -> donkey_button")
            }
            else -> Log.v(TAG,"onClick -> no se que pulsaron")
        }

        //forma mas kotlinera
        //ya no se necesitan las var button
        Log.v(TAG, when (p0?.id){
            R.id.stone_button -> "onClick -> stone_button"
            R.id.donkey_button -> "onClick -> donkey_button"
            else -> "onClick -> no se que pulsaron"
        })

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.v(TAG, "onSaveInstanceState")
//        if (outState != null){
//            outState.putString("clave","valor")
//        }
        outState?.putString("clave", "valor")
   }
}
