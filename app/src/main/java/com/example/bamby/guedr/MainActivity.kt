package com.example.bamby.guedr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.canonicalName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(TAG, "onCreate")
        if (savedInstanceState != null){
            //Log.v(TAG, savedInstanceState.getString("clave"))
            Log.v(TAG, "savedInstanceState !=null y la clave es: ${savedInstanceState.getString("clave")}")
        }else{
            Log.v(TAG, "savedInstanceState == null")
        }

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
