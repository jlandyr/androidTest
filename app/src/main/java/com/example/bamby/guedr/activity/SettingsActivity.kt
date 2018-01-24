package com.example.bamby.guedr.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioGroup
import com.example.bamby.guedr.R

class SettingsActivity: AppCompatActivity (){
    companion object {
        val EXTRA_UNITS = "EXTRA-UNITS"

        //lo mismo que el método de arriba pero más abreviado
        fun intent(context: Context, units: Int): Intent {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(EXTRA_UNITS, units)
            return intent
        }
    }

    val radioGroup by lazy {findViewById<RadioGroup>(R.id.units_rb)}
    //la 1ra vez que se accede a radioGroup, el código entre paréntesis inicia radioGroup y queda inicializado, ya no es null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)//establece la pantalla que quiere mostrar

        findViewById<View>(R.id.ok_button).setOnClickListener{acceptSettings()}
        findViewById<View>(R.id.cancel_button).setOnClickListener{cancelSettings()}
//        radioGroup = findViewById(R.id.units_rb)
        val radioSelected = intent.getIntExtra(EXTRA_UNITS, R.id.celsius_rb)
        radioGroup.check(radioSelected)
    }


    private fun acceptSettings(){
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_UNITS, radioGroup?.checkedRadioButtonId)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }
    private  fun cancelSettings(){
        setResult(Activity.RESULT_CANCELED)
        finish()

    }
}